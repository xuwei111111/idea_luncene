package com.baizhi;

import com.baizhi.dao.PoetriesDAO;
import com.baizhi.entity.Poetries;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class LunceneApplication {
    @Autowired
    private static PoetriesDAO pd;
	public static void main(String[] args) throws IOException {



		SpringApplication.run(LunceneApplication.class, args);


	}





	public static void test1() throws IOException {

		List<Poetries> list = pd.findAll();
		//1.  指定索引文件存储位置 基于内存存储 基于磁盘存储
//  索引文件保存在内存中
// RAMDirectory ramDirectory = new RAMDirectory();
//  保存在磁盘
		FSDirectory fsDirectory = FSDirectory.open(Paths.get("D:\\index\\firstIndex"));
//2.  创建分析器
		StandardAnalyzer standardAnalyzer = new StandardAnalyzer();
//3.  创建索引写入配置对象
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(standardAnalyzer);
//4.  创建索引写入器
		IndexWriter indexWriter = new IndexWriter(fsDirectory,indexWriterConfig);
//5.  创建索引数据

		for ( Poetries p:list) {
			Document document = new Document();
			//循环给document添加域 文本域

			System.out.println(p);
			document.add(new IntField("id", p.getId(), Field.Store.YES));
			document.add(new TextField("content", p.getContent(), Field.Store.YES));
			document.add(new TextField("title",p.getTitle(),Field.Store.YES));
			document.add(new TextField("name",p.getPoets().getName(),Field.Store.YES));



			// 添加索引数据
			indexWriter.addDocument(document);
		}

		indexWriter.commit();
		indexWriter.close();
	}
	public void test2() throws IOException {

		//检索关键字
		String keyword="李";
		//1 找到索引文件目录
		FSDirectory fsDirectory = FSDirectory.open(Paths.get("F:\\第三阶段\\飞秋下载文件夹\\feiq\\Recv Files\\oo1"));
		// 2 创建 Reader
		DirectoryReader directoryReader = DirectoryReader.open(fsDirectory);
		//3 创建索引检索器
		IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
		//创建一个词元对象
		Term term = new Term("name", keyword);
		// 创建TermQuery  基于词元查询
		TermQuery query = new TermQuery(term);
		//4 返回结果
		TopDocs topDocs = indexSearcher.search(query, 10);

		// 5 获取ScoreDocs
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		System.out.println("---------------------------------");
		for (ScoreDoc scoreDoc : scoreDocs) {
			System.out.println("诗人排名:"+scoreDoc.doc);
			Document doc = indexSearcher.doc(scoreDoc.doc);
			System.out.println(doc);
			System.out.println("============================================");
		}


	}


}
