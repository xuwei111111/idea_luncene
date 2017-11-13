package com.baizhi;

import com.baizhi.dao.PoetriesDAO;
import com.baizhi.entity.Poetries;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LunceneApplicationTests {
    @Autowired
    private PoetriesDAO pd;

    @Test
    public void contextLoads() throws IOException {
        List<Poetries> list = pd.findAll();
        //1.  指定索引文件存储位置 基于内存存储 基于磁盘存储
//  索引文件保存在内存中
// RAMDirectory ramDirectory = new RAMDirectory();
//  保存在磁盘
        FSDirectory fsDirectory = FSDirectory.open(Paths.get("D:\\index\\firstIndex"));
//2.  创建分析器
        IKAnalyzer analyzer = new IKAnalyzer();
//3.  创建索引写入配置对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
//4.  创建索引写入器
        IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig);
//5.  创建索引数据

        for (Poetries p : list) {
            Document document = new Document();
            //循环给document添加域 文本域

            System.out.println(p);
            document.add(new IntField("id", p.getId(), Field.Store.YES));
            document.add(new TextField("content", p.getContent(), Field.Store.YES));
            document.add(new TextField("title", p.getTitle(), Field.Store.YES));
            document.add(new TextField("name", p.getPoets().getName(), Field.Store.YES));


            // 添加索引数据
            indexWriter.addDocument(document);
        }

        indexWriter.commit();
        indexWriter.close();

    }

    @Test
    public void test2() throws IOException, ParseException, InvalidTokenOffsetsException {
        int nowPage=2;
        int pageSize=2;
        //1准备检索关键字
        String keyword = "百日依山尽";
        //2 找到索引文件目录
        FSDirectory fsDirectory = FSDirectory.open(Paths.get("D:\\index\\firstIndex"));

        //3 检索索引文件 IndexReader 索引读入器
        IndexReader reader = DirectoryReader.open(fsDirectory);

        // 4 从所以读入器 检索结果 indexSearcher
        IndexSearcher indexSearcher = new IndexSearcher(reader);

        //5 在通过索引检索器 检查索引
        // 参数一 : 查询的条件query 需要将查询字符串 封装到query对象中
        // 参数二: 代表需要查询几台哦结果
        QueryParser queryParser = new QueryParser("content", new StandardAnalyzer());

        Query query = queryParser.parse(keyword);

        QueryScorer scorer = new QueryScorer(query);

        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");

        Highlighter highlighter = new Highlighter(formatter, scorer);

        // 6 检索结果处理 封装了查询结果

        TopDocs topDocs = indexSearcher.search(query, 50);
        // 查看总命中数

        int totalHits = topDocs.totalHits;
        System.out.println("查询到的总条数:" + totalHits);

        // 文档信息  全文检索相关度排序 最符合条件的排在最前面

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("文档的得分:" + scoreDoc.score);
            //返回文档在索引库中的编号
            int id = scoreDoc.doc;
            //通过文档编号恢复文档信息
            Document document = reader.document(id);

            String bestFragment = highlighter.getBestFragment(new StandardAnalyzer(), "content", document.get("content"));

            System.out.println(document.get("id") + " | " + document.get("title") + " | " + document.get("name") + " | " + bestFragment);

        }
        reader.close();

    }


}
