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
int sw = 0;

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

//キーボード操作によりswが変化し、それによって描写を変える
//

void draw() {
	if(img != null){
		if (sw == 1) {
			histogram();
		}else if (sw == 0) {
			image(img,0,0);
			ChangeWindowSize(img.width, img.height);
		}
		//println(img.width,img.height);
	}else{
		//font = loadFont("console");
		textSize(18);
		text("ここにD and D",50,50);
	}
}

/**
 * サイズに合わせて画面サイズを変更する
 * @param {value} int w 画像の横幅
 * @param {value} int h 画像の縦幅
 */
void ChangeWindowSize(int w,int h){
	frame.setSize(w + frame.getInsets().left + frame.getInsets().right,h + frame.getInsets().top + frame.getInsets().bottom);
	size(w,h);
}

void keyPressed(){
	println(key);
	if (key == 'h'){
		sw = 1;
		redraw();
	}else if (key == 's'){
		save("data.png");
	}else if (key == 'r'){
		sw = 0;
		redraw();
	}
}

/**
 * ヒストグラムを生成する
 * @return ヒストグラムを表示
 */
void histogram(){
	int[] h = new int[256];
	for(int y = 0;y < img.height;y++){
		for(int x = 0;x < img.width;x++){
			h[int(brightness(img.get(x,y)))]++;
		}
	}
	//loop();
	ChangeWindowSize(512,288);
	background(255);
	for(int i = 0; i < h.length - 1;i++){
		line(i*3,map(h[i],max(h),0,0,height+10),(i+1)*3,map(h[i+1],max(h),0,0,height+10));
	}
}