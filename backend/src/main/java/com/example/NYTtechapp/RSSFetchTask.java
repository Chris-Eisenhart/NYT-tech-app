import org.springframework.stereotype.Component;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


@Component
public class RSSFetchTask {

    @Autowired
    private RestTemplate restTemplate;

    @Cacheable(value = "nytTechCache", key = "#root.methodName", unless = "#result == null")
    public List<Article> fetchDataFromRSSFeed() {
        // Fetch data from the NYT Technology RSS feed
        String rssFeedUrl = "https://rss.nytimes.com/services/xml/rss/nyt/Technology.xml";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(rssFeedUrl, String.class);
        String rssData = responseEntity.getBody();

        // Parse the XML and extract the required data
        List<Article> articles = parseRSSData(rssData);

        // Return the fetched data
        return articles;
    }

    private List<Article> parseRSSData(String rssData) {
        List<Article> articles = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(rssData));
            Document document = builder.parse(inputSource);

            // Extract the required data from the XML document
            NodeList itemNodes = document.getElementsByTagName("item");
            for (int i = 0; i < itemNodes.getLength(); i++) {
                Element itemElement = (Element) itemNodes.item(i);

                String title = getElementTextByTag(itemElement, "title");
                String link = getElementTextByTag(itemElement, "link");
                String description = getElementTextByTag(itemElement, "description");
                String pubDate = getElementTextByTag(itemElement, "pubDate");
                String author = getElementTextByTag(itemElement, "dc:creator");

                // Create an Article object with the extracted data
                Article article = new Article(title, link, description, pubDate, author);

                // Add the Article object to the articles list
                articles.add(article);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return articles;
    }

    private String getElementTextByTag(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }

    // Rest of the class
}

