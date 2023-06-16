import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final RSSFetchTask rssFetchTask;

    @Autowired
    public ArticleController(RSSFetchTask rssFetchTask) {
        this.rssFetchTask = rssFetchTask;
    }

    @GetMapping
    public List<Article> getArticles() {
        return rssFetchTask.fetchDataFromRSSFeed();
    }
}

