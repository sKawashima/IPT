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

DropTarget dropTarget;
PImage img;

void setup(){
  // てきとうにサイズ設定
  size(512,512);
  // ==================================================
  // ファイルのドラッグ&ドロップをサポートするコード
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
          /* 例外処理 */
        } catch (IOException ex) {
          /* 例外処理 */
        }
      }
      if(fileNameList == null) return;
      // ドラッグ&ドロップされたファイルの一覧は一時リストに格納される
      // 以下のように書くと、ファイルのフルパスを表示
      for(File f : fileNameList){
      	img=loadImage(f.getAbsolutePath());
      	redraw();
      }
    }
  });
  // ==================================================
  noLoop();
}

void draw() {
  if(img != null){
    image(img,0,0);
    ChangeWindowSize(img.width, img.height);
    //println(img.width,img.height);
  }else{
    //font = loadFont("console");
    textSize(18);
    text("ここにD and D",50,50);
  }
}

void ChangeWindowSize(int w,int h){
  frame.setSize(w + frame.getInsets().left + frame.getInsets().right,h + frame.getInsets().top + frame.getInsets().bottom);
  size(w,h);
}