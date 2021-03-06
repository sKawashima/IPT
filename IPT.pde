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
import java.awt.Frame;

DropTarget dropTarget;
PImage img;
int sw = 0;//画面遷移のid
int p = 50;//pタイル法におけるp値(p%として取る)
String fname="";//最後に開いた画像の名前
String ip="";
int[] h = new int[256];//どの色がどれだけ出てきたかを格納する配列

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
				fname = f.getName();
				sw = 0;
				frame.setTitle("loaded");
				p = 50;
				cal_h();
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
			frame.setTitle("h: histogram");
			histogram();
		}else if (sw == 0) {
			image(img,0,0);
			ChangeWindowSize(img.width, img.height);
		}else if (sw == 2) {
			p_check();
		}
		//println(img.width,img.height);
	}else{
		//font = loadFont("console");
		textSize(30);
		background(255);
		fill(0);
		textAlign(CENTER,CENTER);
		text("D and D jpeg image here",width/2,height/2);
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
	//println(key);
	if (key == 'h'){
		sw = 1;
		redraw();
	}else if (key == 's'){
		save(fname + " - " + ip + ".png");
		frame.setTitle("saved");
	}else if (key == 'r'){
		sw = 0;
		redraw();
	}else if (key == 'p') {
		sw = 2;
		redraw();
	}else if(keyCode == UP){
		if(sw == 2){
			p--;
			redraw();
		}
	}else if(keyCode == DOWN){
		if(sw == 2){
			p++;
			redraw();
		}
	}
}

/**
 * ふちなし四角形の作成
 * @param  {value} int a 四角形の左上のx座標
 * @param  {value} int b 四角形の左上のy座標
 * @param  {value} int c 四角形の右下のx座標
 * @param  {value} int d 四角形の右下のy座標
 */
void rectn(int a,int b,int c, int d){
	line(a,b,a,d-1);
	line(a,b,c-1,b);
	line(c-1,b,c-1,d-1);
	line(a,d-1,c-1,d-1);
}

/**
 * h[]の計算
 * @return none
 */
void cal_h(){
	for(int i = 0;i < 256;i++){
		h[i] = 0;
	}

	for(int y = 0;y < img.height;y++){
		for(int x = 0;x < img.width;x++){
			h[int(brightness(img.get(x,y)))]++;
		}
	}
}

/**
 * ヒストグラムを生成する
 * @return ヒストグラムを表示
 */
void histogram(){
	ChangeWindowSize(768,288);
	background(255);
	fill(0);
	rectn(0,0,width,height);
	for(int i = 0; i < h.length - 1;i++){
		line(i*3,map(h[i],max(h),0,0,height+10),(i+1)*3,map(h[i+1],max(h),0,0,height+10));
	}
	ip = "histogram";
}

/**
 * p値からどの明るさまでを扱うか確定する
 * @return none
 */
void p_check(){
	int all = img.width*img.height;//全体画素値
	int p_ch = all * p / 100;//いくつの画素値を活用するか確定
	int sum = 0;
	int r = 0;
	for(int i = 0;i < 256;i++){
		sum += h[i];
		if(sum >= p_ch){
			r = i;//どの明るさまで使うかを格納する
			break;
		}
	}
	p_draw(r);
	frame.setTitle("p: p-tile p: " + (100 - p) + " r: " + r);
	ip = "p-tile p= " + (100 - p);
	//print(r);
}

void p_draw(int r){
	ChangeWindowSize(img.width, img.height);
	background(255);
	//print("r:"+r);
	//println(int(brightness(img.get(10,10))));
	for(int x = 0;x < img.width;x++){
		for(int y = 0;y < img.height;y++){
			if(int(brightness(img.get(x,y))) > r) ;
			else point(x,y);
		}
	}
}