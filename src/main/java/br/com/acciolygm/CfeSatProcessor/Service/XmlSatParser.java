package br.com.acciolygm.CfeSatProcessor.Service;

import br.com.acciolygm.CfeSatProcessor.Model.Cupom;
import br.com.acciolygm.CfeSatProcessor.Model.Item;
import br.com.acciolygm.CfeSatProcessor.Utils.XmlUtils;

import org.w3c.dom.*;
import javax.xml.xpath.*;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Classe responsável por interpretar e extrair informações fiscais de um XML de CF-e SAT.
 *
 * O parser lê o conteúdo do XML ,
 * mapeia para objetos e devolve o resultado
 * pronto para ser salvo em banco de dados.
 */
public class XmlSatParser {
    private final XPath xp = XPathFactory.newInstance().newXPath();

    public static class ParseResult {
        public boolean canceld;
        public Cupom cupom;
    }

    public ParseResult parse(File file) throws Exception {
        Document doc = XmlUtils.parse(file);
        String root = doc.getDocumentElement().getTagName();

        ParseResult result = new ParseResult();
        // Regra: ignorar cupons cancelados (CFeCanc). Se for evento de cancelamento, marcar cancelado.
        if ("CFeCanc".equalsIgnoreCase(root)) {
            result.canceld = true;
            return result;
        }

        // Chave de acesso: atributo Id de /CFe/infCFe, ex: "CFe3524...".
        String id = attr(doc, "/CFe/infCFe", "Id");
        String Acceskey = (id != null && id.startsWith("CFe")) ? id.substring(3) : id;

        // Número do CF-e
        String Cfe = text(doc, "/CFe/infCFe/ide/nCFe");

        // Data/hora emissão
        String issuedD = text(doc, "/CFe/infCFe/ide/dEmi");
        String issuedH = text(doc, "/CFe/infCFe/ide/hEmi");
        LocalDateTime issuedDate = LocalDateTime.parse(issuedD + issuedH, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        // Valor total do cupom
        String vCFe = text(doc, "/CFe/infCFe/total/vCFe");
        // Alguns XMLs trazem vCFe em outro nó; adaptar se necessário.

        Cupom cupom = Cupom.builder()
                .accessKey(Acceskey)
                .cfe(Cfe)
                .issuedAt(issuedDate)
                .total(new BigDecimal(vCFe))
                .items(new ArrayList<>())
                .build();

        // Itens: /CFe/infCFe/det
        NodeList items = nodes(doc, "/CFe/infCFe/det");
        for (int i = 0; i < items.getLength(); i++) {
            Node det = items.item(i);
            String code = text(det, "prod/cProd");
            String description = text(det, "prod/xProd");
            String cfo = text(det, "prod/CFOP");
            String comQ = text(det, "prod/qCom");
            String itemV = text(det, "prod/vItem");

            Item item = Item.builder()
                    .cupom(cupom)
                    .code(code)
                    .description(description)
                    .cfop(cfo)
                    .quantity(new BigDecimal(comQ))
                    .grossTotal(new BigDecimal(itemV))
                    .build();

            cupom.getItems().add(item);
        }

        result.cupom = cupom;
        return result;
    }

    private String text(Object context, String path) throws Exception {
        String s = (String) xp.evaluate(path, context, XPathConstants.STRING);
        return s == null ? null : s.trim();
    }

    private NodeList nodes(Object context, String path) throws Exception {
        return (NodeList) xp.evaluate(path, context, XPathConstants.NODESET);
    }

    private String attr(Object context, String path, String attr) throws Exception {
        Node n = (Node) xp.evaluate(path, context, XPathConstants.NODE);
        if (n != null && n.getAttributes() != null) {
            Node a = n.getAttributes().getNamedItem(attr);
            if (a != null) return a.getNodeValue();
        }
        return null;
    }
}

