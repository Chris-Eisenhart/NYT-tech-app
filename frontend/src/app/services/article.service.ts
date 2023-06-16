import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  private rssUrl = 'https://rss.nytimes.com/services/xml/rss/nyt/Technology.xml';

  constructor(private http: HttpClient) { }

  getArticles(): Observable<Article[]> {
    return this.http.get(this.rssUrl, { responseType: 'text' }).pipe(
      map((xmlData: string) => {
        const parser = new DOMParser();
        const xmlDoc = parser.parseFromString(xmlData, 'text/xml');
        const items = xmlDoc.getElementsByTagName('item');
        const articles: Article[] = [];

        for (let i = 0; i < items.length; i++) {
          const item = items[i];
          const title = item.getElementsByTagName('title')[0].textContent;
          const description = item.getElementsByTagName('description')[0].textContent;
          const image = item.getElementsByTagName('media:content')[0].getAttribute('url');
          const link = item.getElementsByTagName('link')[0].textContent;

          articles.push({ title, description, image, link });
        }

        return articles;
      }),
      catchError(() => of([]))
    );
  }
}

export interface Article {
  title: string;
  description: string;
  image: string;
  link: string;
}

