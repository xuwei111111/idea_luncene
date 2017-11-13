package com.baizhi.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/9.
 */
@Controller
@RequestMapping("/lucene")
public class Lucene {
     @RequestMapping("/find")
    public String  find(HttpSession s, Model m, Integer nowPage, String keyWord) throws IOException, InvalidTokenOffsetsException, ParseException {
         IKAnalyzer ikAnalyzer = new IKAnalyzer();
         //nowPage=2;
         if(nowPage==null){
             nowPage=1;

         }
         if(keyWord==null){
             keyWord= (String) s.getAttribute("keyWord");
         }else {
             s.setAttribute("keyWord", keyWord);
         }
         int pageSize=10;
        //1准备检索关键字
        //String keyword = "百日依山尽";
        //2 找到索引文件目录
        FSDirectory fsDirectory = FSDirectory.open(Paths.get("D:\\index\\firstIndex"));

        //3 检索索引文件 IndexReader 索引读入器
        IndexReader reader = DirectoryReader.open(fsDirectory);

        // 4 从所以读入器 检索结果 indexSearcher
        IndexSearcher indexSearcher = new IndexSearcher(reader);

        //5 在通过索引检索器 检查索引
        // 参数一 : 查询的条件query 需要将查询字符串 封装到query对象中
        // 参数二: 代表需要查询几台哦结果

        QueryParser queryParser = new QueryParser("content", ikAnalyzer);
         QueryParser queryParser2 = new QueryParser("title", ikAnalyzer);
         QueryParser queryParser3 = new QueryParser("name", ikAnalyzer);

         Query query = queryParser.parse(keyWord);
         //Query query2 = queryParser2.parse(keyWord);
        // Query query3 = queryParser3.parse(keyWord);

        QueryScorer scorer = new QueryScorer(query);
         //QueryScorer scorer2 = new QueryScorer(query2);
        // QueryScorer scorer3 = new QueryScorer(query3);

        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");

        Highlighter highlighter = new Highlighter(formatter, scorer);
         //Highlighter highlighter2 = new Highlighter(formatter, scorer2);
         //Highlighter highlighter3 = new Highlighter(formatter, scorer3);

        // 6 检索结果处理 封装了查询结果
        TopDocs topDocs=null;
        int totalHits=0;
        if(nowPage>1) {
            topDocs = indexSearcher.search(query, (nowPage - 1) * pageSize);
            //获取上一页最后一个ScoreDoc对象
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            ScoreDoc LscoreDoc = scoreDocs[scoreDocs.length - 1];
            //通过上一页最后一条数据查找下一条数据
             topDocs = indexSearcher.searchAfter(LscoreDoc, query, pageSize);

        }else{
            topDocs=indexSearcher.search(query,pageSize);
        }
        // 查看总命中数
        totalHits = topDocs.totalHits;

        System.out.println("查询到的总条数:" + totalHits);

        // 文档信息  全文检索相关度排序 最符合条件的排在最前面

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        ArrayList<Map> list = new ArrayList<>();
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("文档的得分:" + scoreDoc.score);
            //返回文档在索引库中的编号
            int id = scoreDoc.doc;
            //通过文档编号恢复文档信息
            Document document = reader.document(id);


            String bestFragment = highlighter.getBestFragment(ikAnalyzer, "content", document.get("content"));
          //  String title = highlighter2.getBestFragment(ikAnalyzer, "title", document.get("title"));
           // String name = highlighter3.getBestFragment(ikAnalyzer, "name", document.get("name"));
            HashMap<String, String> map = new HashMap<>();
            map.put("title",document.get("title"));
             map.put("name",document.get("name"));
            map.put("content",bestFragment);
            list.add(map);
            System.out.println(document.get("id") + " | " +document.get("title") + " | " + document.get("name")+ " | " + bestFragment);

        }

        m.addAttribute("poetries",list);
         m.addAttribute("page",nowPage);
        reader.close();

       return "baidu";

    }

}
