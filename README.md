# ChatGPT Java

[![Maven Central](https://img.shields.io/maven-central/v/com.lilittlecat/chatgpt?style=for-the-badge)](https://central.sonatype.com/artifact/com.lilittlecat/chatgpt)



A Java client for the [ChatGPT API](https://platform.openai.com/docs/guides/chat).

## Change Log

### 1.0.3 - 2023-12-04

- fix [#17](https://github.com/LiLittleCat/ChatGPT/issues/17)

### 1.0.2 - 2023-04-24
- Support message history

### 1.0.1
- Support GPT-4
- Add custom API host

### 1.0.0
- Use official API with model `gpt-3.5-turbo`
- Support proxy

### 0.1.0
- Initial release
- Use unofficial API


## Download

Download [the latest JAR](https://search.maven.org/remote_content?g=com.lilittlecat&a=chatgpt&v=LATEST) or grab via [Maven](https://central.sonatype.dev/artifact/com.lilittlecat/chatgpt/1.0.0):

```xml
<dependency>
  <groupId>com.lilittlecat</groupId>
  <artifactId>chatgpt</artifactId>
  <version>1.0.2</version>
</dependency>
```

or Gradle:

```groovy
implementation 'com.lilittlecat:chatgpt:1.0.2'
```

## Usage

```java
import com.lilittlecat.chatgpt.offical.ChatGPT;

public class Main {
   public static void main(String[] args) {
      ChatGPT chatGPT = new ChatGPT("YOUR API KEY");
      String hello = chatGPT.ask("hello");
      System.out.println(hello); // will be "\n\nHello! How may I assist you today?"
   }
}
```

More examples can be found in the [test](./src/test/java/ChatGPTTest.java).

## Happy coding
