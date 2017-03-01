import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {
  public static enum typeOfToken {
    KEYWORD("int|class|constructor|method|function|boolean|char|void|var|static|field|let|do|if|else|while|return|true|false|null|this"), WHITESPACE("[ \t\f\r\n]+"),  SYMBOL("[{}()-*/&|~<>;,.=]"), IDENTIFIER("[a-zA-Z0-9]");

    public final String pattern;

    private typeOfToken(String pattern) {
      this.pattern = pattern;
    }
  }

  public static class Token {
    public typeOfToken type;
    public String input;

    public Token(typeOfToken type, String data) {
      this.type = type;
      this.input = data;
    }

    @Override
    public String toString() {
      return String.format("<%s %s>", type.name(), input);
    }
  }

  public static ArrayList<Token> parse(String input) {
    ArrayList<Token> tokens = new ArrayList<Token>();

    StringBuffer tokenBuffer = new StringBuffer();
    for (typeOfToken typeOfToken : typeOfToken.values())
      tokenBuffer.append(String.format("|(?<%s>%s)", typeOfToken.name(), typeOfToken.pattern));
    Pattern tokenPatterns = Pattern.compile(new String(tokenBuffer.substring(1)));

    Matcher matcher = tokenPatterns.matcher(input);
    while (matcher.find()) {
      if (matcher.group(typeOfToken.IDENTIFIER.name()) != null) {
        tokens.add(new Token(typeOfToken.IDENTIFIER, matcher.group(typeOfToken.IDENTIFIER.name())));
        continue;
      } else if (matcher.group(typeOfToken.KEYWORD.name()) != null) {
        tokens.add(new Token(typeOfToken.KEYWORD, matcher.group(typeOfToken.KEYWORD.name())));
        continue;
      } else if (matcher.group(typeOfToken.WHITESPACE.name()) != null)
        continue;
     	else if (matcher.group(typeOfToken.SYMBOL.name()) != null)
     	{
         tokens.add(new Token(typeOfToken.SYMBOL, matcher.group(typeOfToken.SYMBOL.name())));
         continue;
     	}
    }

    return tokens;
  }

  public static void main(String[] args) {
    String input = "";
	try {
		input = readFile("C:/file.txt");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

    ArrayList<Token> tokens = parse(input);
    for (Token token : tokens)
      System.out.println(token);
  }
  
  private static String readFile(String pathname) throws IOException {

	    File file = new File(pathname);
	    StringBuilder fileContents = new StringBuilder((int)file.length());
	    Scanner scanner = new Scanner(file);
	    String lineSeparator = System.getProperty("line.separator");

	    try {
	        while(scanner.hasNextLine()) {
	            fileContents.append(scanner.nextLine() + lineSeparator);
	        }
	        return fileContents.toString();
	    } finally {
	        scanner.close();
	    }
  }
}
