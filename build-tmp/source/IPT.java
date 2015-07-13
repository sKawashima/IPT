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
import java.awt.Frame; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class IPT extends PApplet {















DropTarget dropTarget;
PImage img;
int sw = 0;//\u753b\u9762\u9077\u79fb\u306eid
int p = 50;//p\u30bf\u30a4\u30eb\u6cd5\u306b\u304a\u3051\u308bp\u5024(p%\u3068\u3057\u3066\u53d6\u308b)
String fname="";//\u6700\u5f8c\u306b\u958b\u3044\u305f\u753b\u50cf\u306e\u540d\u524d
String ip="";
int[] h = new int[256];//\u3069\u306e\u8272\u304c\u3069\u308c\u3060\u3051\u51fa\u3066\u304d\u305f\u304b\u3092\u683c\u7d0d\u3059\u308b\u914d\u5217

public void setup(){
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

//\u30ad\u30fc\u30dc\u30fc\u30c9\u64cd\u4f5c\u306b\u3088\u308asw\u304c\u5909\u5316\u3057\u3001\u305d\u308c\u306b\u3088\u3063\u3066\u63cf\u5199\u3092\u5909\u3048\u308b
//

public void draw() {
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
 * \u30b5\u30a4\u30ba\u306b\u5408\u308f\u305b\u3066\u753b\u9762\u30b5\u30a4\u30ba\u3092\u5909\u66f4\u3059\u308b
 * @param {value} int w \u753b\u50cf\u306e\u6a2a\u5e45
 * @param {value} int h \u753b\u50cf\u306e\u7e26\u5e45
 */
public void ChangeWindowSize(int w,int h){
	frame.setSize(w + frame.getInsets().left + frame.getInsets().right,h + frame.getInsets().top + frame.getInsets().bottom);
	size(w,h);
}

public void keyPressed(){
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
 * \u3075\u3061\u306a\u3057\u56db\u89d2\u5f62\u306e\u4f5c\u6210
 * @param  {value} int a \u56db\u89d2\u5f62\u306e\u5de6\u4e0a\u306ex\u5ea7\u6a19
 * @param  {value} int b \u56db\u89d2\u5f62\u306e\u5de6\u4e0a\u306ey\u5ea7\u6a19
 * @param  {value} int c \u56db\u89d2\u5f62\u306e\u53f3\u4e0b\u306ex\u5ea7\u6a19
 * @param  {value} int d \u56db\u89d2\u5f62\u306e\u53f3\u4e0b\u306ey\u5ea7\u6a19
 */
public void rectn(int a,int b,int c, int d){
	line(a,b,a,d-1);
	line(a,b,c-1,b);
	line(c-1,b,c-1,d-1);
	line(a,d-1,c-1,d-1);
}

/**
 * h[]\u306e\u8a08\u7b97
 * @return none
 */
public void cal_h(){
	for(int i = 0;i < 256;i++){
		h[i] = 0;
	}

	for(int y = 0;y < img.height;y++){
		for(int x = 0;x < img.width;x++){
			h[PApplet.parseInt(brightness(img.get(x,y)))]++;
		}
	}
}

/**
 * \u30d2\u30b9\u30c8\u30b0\u30e9\u30e0\u3092\u751f\u6210\u3059\u308b
 * @return \u30d2\u30b9\u30c8\u30b0\u30e9\u30e0\u3092\u8868\u793a
 */
public void histogram(){
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
 * p\u5024\u304b\u3089\u3069\u306e\u660e\u308b\u3055\u307e\u3067\u3092\u6271\u3046\u304b\u78ba\u5b9a\u3059\u308b
 * @return none
 */
public void p_check(){
	int all = img.width*img.height;//\u5168\u4f53\u753b\u7d20\u5024
	int p_ch = all * p / 100;//\u3044\u304f\u3064\u306e\u753b\u7d20\u5024\u3092\u6d3b\u7528\u3059\u308b\u304b\u78ba\u5b9a
	int sum = 0;
	int r = 0;
	for(int i = 0;i < 256;i++){
		sum += h[i];
		if(sum >= p_ch){
			r = i;//\u3069\u306e\u660e\u308b\u3055\u307e\u3067\u4f7f\u3046\u304b\u3092\u683c\u7d0d\u3059\u308b
			break;
		}
	}
	p_draw(r);
	frame.setTitle("p: p-tile p: " + (100 - p) + " r: " + r);
	ip = "p-tile p= " + (100 - p);
	//print(r);
}

public void p_draw(int r){
	ChangeWindowSize(img.width, img.height);
	background(255);
	//print("r:"+r);
	//println(int(brightness(img.get(10,10))));
	for(int x = 0;x < img.width;x++){
		for(int y = 0;y < img.height;y++){
			if(PApplet.parseInt(brightness(img.get(x,y))) > r) ;
			else point(x,y);
		}
	}
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "IPT" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
