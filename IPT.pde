PImage img;

void setup(){
	//size(512, 512);
	img = loadImage("sample1.jpg");
	size(img.width, img.height);
	noLoop();
}

void draw(){
	image(img,0,0);
}
