Polonaise polonaise;

void settings() {
  size(1400, 800);
}

void setup() { 
  polonaise = new Polonaise();
}

void draw() {
  background(0);
  polonaise.start();
  noCursor();
}

void mouseClicked(){
        polonaise.a = polonaise.a + 1;
      }
