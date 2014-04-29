/* Daniel Shiffman               */
/* Programming from A to Z       */
/* Spring 2006                   */
/* http://www.shiffman.net       */
/* daniel.shiffman@nyu.edu       */

package readFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class WriteFile {
	  private String filename;
	  
	  public WriteFile(String name) {
	    filename = name;
	  }
	  
	  public void writeContent(String content) throws IOException {
	    // Create an output stream and file channel
	    // Using second argument as file name to write out
	    FileOutputStream fos = new FileOutputStream(filename);
	    FileChannel outfc = fos.getChannel();
	    
	    // Convert content String into ByteBuffer and write out to file
	    ByteBuffer bb = ByteBuffer.wrap(content.getBytes());
	    //outfc.position(0);
	    outfc.write(bb);
	    outfc.close();
	  }	  
}
