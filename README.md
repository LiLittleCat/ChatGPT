# ChatGPT

A Java client for the unofficial [ChatGPT](https://chat.openai.com/) API.

> Inspired by [PyChatGPT](https://github.com/rawandahmad698/PyChatGPT) and [chatgpt-api](https://github.com/transitive-bullshit/chatgpt-api), thanks a lot.


## Download

Download [the latest JAR](https://search.maven.org/remote_content?g=com.lilittlecat&a=chatgpt&v=LATEST) or grab via [Maven](https://central.sonatype.dev/artifact/com.lilittlecat/chatgpt/0.1.0):

```xml
<dependency>
  <groupId>com.lilittlecat</groupId>
  <artifactId>chatgpt</artifactId>
  <version>0.1.0</version>
</dependency>
```

or Gradle:

```groovy
implementation 'com.lilittlecat:chatgpt:0.1.0'
```

## Usage

> **Note**
> In the development process, it is found that the APIs from [PyChatGPT](https://github.com/rawandahmad698/PyChatGPT) are not very stable.
>
> At present, only use by set token manually, and the email and password login is supported subsequently.


```java
import com.lilittlecat.chatgpt.ChatGPT;

public class Main {
    public static void main(String[] args) {
       ChatGPT chatGPT = new ChatGPT("sessionToken");
       String hello = chatGPT.ask("hello");
       System.out.println(hello); // will be "Hello! I'm Assistant, a large language model trained by OpenAI. I'm here to help answer any questions you might have. What can I help you with today?"
    }
}
```

### Session Tokens

**This package requires a valid session token from ChatGPT to access it's unofficial REST API.**

To get a session token:

1. Go to https://chat.openai.com/chat and log in or sign up.
2. Open dev tools.
3. Open `Application` > `Cookies`.
   ![ChatGPT cookies](./media/session-token.png)
4. Copy the value for `__Secure-next-auth.session-token` as parameter "sessionToken".

## Happy coding
