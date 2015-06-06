import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.awt.datatransfer.DataFlavor; 
import java.awt.datatransfer.Transferable; 
import java.awt.datatransfer.UnsupportedFlavorException; 
import java.awt.dnd.DnDConstants; 
import java.awt.dnd.DropTarget; 
import java.awt.dnd.DropTargetDragEvent; 
import java.awt.dnd.DropTargetDropEvent; 
import java.awt.dnd.DropTargetEvent; 
import java.awt.dnd.DropTargetListener; 
import java.io.File; 
import java.io.IOException; 
import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class DandDImage extends PApplet {














DropTarget dropTarget;
PImage img;

public void setup()  {
  
  // \u3066\u304d\u3068\u3046\u306b\u30b5\u30a4\u30ba\u8a2d\u5b9a
  size(512,512);
  
  // ==================================================
  // \u30d5\u30a1\u30a4\u30eb\u306e\u30c9\u30e9\u30c3\u30b0&\u30c9\u30ed\u30c3\u30d7\u3092\u30b5\u30dd\u30fc\u30c8\u3059\u308b\u30b3\u30fc\u30c9
  // ==================================================
  dropTarget = new DropTarget(this, new DropTargetListener() {
    public void dragEnter(DropTargetDragEvent dtde) {}
    public void dragOver(DropTargetDragEvent dtde) {}
    public void dropActionChanged(DropTargetDragEvent dtde) {}
    public void dragExit(DropTargetEvent dte) {}
    public void drop(DropTargetDropEvent dtde) {
      dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
      Transferable trans = dtde.getTransferable();
      List<File> fileNameList = null;
      if(trans.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
        try {
          fileNameList = (List<File>)
            trans.getTransferData(DataFlavor.javaFileListFlavor);
        } catch (UnsupportedFlavorException ex) {
          /* \u4f8b\u5916\u51e6\u7406 */
        } catch (IOException ex) {
          /* \u4f8b\u5916\u51e6\u7406 */
        }
      }
      if(fileNameList == null) return;
      // \u30c9\u30e9\u30c3\u30b0&\u30c9\u30ed\u30c3\u30d7\u3055\u308c\u305f\u30d5\u30a1\u30a4\u30eb\u306e\u4e00\u89a7\u306f\u4e00\u6642\u30ea\u30b9\u30c8\u306b\u683c\u7d0d\u3055\u308c\u308b
      // \u4ee5\u4e0b\u306e\u3088\u3046\u306b\u66f8\u304f\u3068\u3001\u30d5\u30a1\u30a4\u30eb\u306e\u30d5\u30eb\u30d1\u30b9\u3092\u8868\u793a
      for(File f : fileNameList){
      	img=loadImage(f.getAbsolutePath());
      }
    }
  });
  // ==================================================
}

public void draw() {
  if(img != null){
    image(img,0,0);
    ChangeWindowSize(img.width, img.height);
    //println(img.width,img.height);
  }else{
    //font = loadFont("console");
    textSize(18);
    text("\u3053\u3053\u306bD and D",50,50);
  }
}

public void ChangeWindowSize(int w,int h){
  frame.setSize(w + frame.getInsets().left + frame.getInsets().right,h + frame.getInsets().top + frame.getInsets().bottom);
  size(w,h);
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "DandDImage" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
