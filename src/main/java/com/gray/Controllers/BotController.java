package com.gray.Controllers;

import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/honeypot")
public class BotController {

	public volatile boolean running=true;
	@GetMapping("/fake-news")
	public ResponseEntity<?> trapBot() throws Exception
	{

		
	    String json=null;
		StringBuilder sb= new StringBuilder(500);

		//sb.append("{\\\"メッセージ一覧\\\":[");
		for(int i=0;i<5_000_000_00&&running;i++)
		{
		
			String data = randomJapanese(30);   // 30 random JP chars
		    String word = randomJapaneseWord(); // random Japanese word
		    String sentence = randomJapaneseSentence(); // random sentence
			
			 json = "{"
		            + "\"テキスト\":\"" + data + "\","
		            + "\"単語\":\"" + word + "\","
		            + "\"文章\":\"" + sentence + "\""
		            + "}";
			//sb.append("\"データ番号: ").append(i).append("\",");
			 sb.append(json);
			 Thread.sleep(300);
		}
		//sb.append("\\\"終わり\\\"]}");
		return ResponseEntity.ok()
				.header("Content-Type", "application/json")
				.body(sb.toString());
	}
	
//	@GetMapping("/admin")
//	public void slowStreas(HttpServletResponse response) throws Exception
//	{
//		
//		response.setContentType("text/plain");
//		for(int i=0;i<5000000;i++)
//		{
//			response.getWriter().write("Chunk"+i+"<--");
//			response.flushBuffer();
//			Thread.sleep(1000);
//		}
//	}
	//random chars
	private String randomJapanese(int length) {
	    String chars = "あいうえおかきくけこさしすせそたちつてとなにぬねのまみむめもやゆよらりるれろわをんアイウエオカキクケコサシスセソタチツテトナニヌネノマミムメモヤユヨラリルレロワヲン";

	    StringBuilder sb = new StringBuilder();

	    Random r = new Random();

	    for (int i = 0; i < length; i++) {
	        sb.append(chars.charAt(r.nextInt(chars.length())));
	    }

	    return sb.toString();
	}
	
	//random japness word
	private String randomJapaneseWord() {
	    String[] words = {
	        "こんにちは", "ありがとう", "お願いします", "すごい", "大丈夫",
	        "天気", "学校", "時間", "未来", "希望",
	        "山", "海", "空", "光", "夢",
	        "東京", "日本", "桜", "文化", "伝統"
	    };

	    return words[new Random().nextInt(words.length)];
	}

	//random japanesse sentence 
	private String randomJapaneseSentence() {
	    String[] parts1 = {"私は", "あなたは", "彼は", "彼女は", "みんなは"};
	    String[] parts2 = {"静かに", "ゆっくりと", "突然", "深く", "優しく"};
	    String[] parts3 = {
	        "歩いています", "考えています", "笑っています", 
	        "話しています", "見つめています", "眠っています"
	    };

	    Random r = new Random();
	    return parts1[r.nextInt(parts1.length)]
	         + parts2[r.nextInt(parts2.length)]
	         + parts3[r.nextInt(parts3.length)] + "。";
	}

	
	
}
