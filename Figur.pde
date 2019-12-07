public abstract class Figur {
  PVector pos;
  abstract void zeich(Figur fig);
}

public abstract class FigurImpl extends Figur {
  FigurImpl() {
    pos = new PVector(700, 700);
  }
  abstract void zeich(Figur fig);
}

class Maus extends FigurImpl {
  void zeich(Figur fig) {
    pos.x = mouseX;
    pos.y = mouseY;
    triangle(pos.x-10, pos.y-10, pos.x, pos.y+10, pos.x+10, pos.y-10);
  }
  
}

class Kreis extends FigurImpl implements Movement {
  boolean falling;
  boolean walking;
  boolean folgen;
  float speed = 1.5;
  int n = 0;
  void zeich(Figur fig) {
    stroke(255);
    noFill();
    ellipse(this.pos.x, this.pos.y, 20, 20);
    line(this.pos.x, this.pos.y, fig.pos.x, fig.pos.y );
  }
  
  void folgen(Figur fig) {
    float diffX = fig.pos.x - this.pos.x;
    float diffY = fig.pos.y - this.pos.y;
    if (sqrt(pow(diffX, 2) + pow(diffY, 2)) > 50) {
      this.pos.x += diffX / 15;
      this.pos.y += diffY / 15;
    }
    if (this.pos.y<200){
      falling = true;
    }
    
  }
  
  void laufen(){
    
    if(this.pos.x>700){
      this.pos.x = this.pos.x + speed;
      if(this.pos.x>1390){
        System.out.println("Läufer hat rechtes Ziel erreicht!"); 
        folgen = true;
      }
    } else {
      this.pos.x=this.pos.x - speed;
      if(this.pos.x<10){
        System.out.println("Läufer hat linkes Ziel erreicht!");
        folgen = true;
      }
    }
  
  }
  
  void fallen(){

          n++;
          if(n<40){
            this.pos.x=this.pos.x + 0.8;
          } else if(n<80) {
            this.pos.x=this.pos.x - 0.7; 
          } else { n = n - 80;}
          this.pos.y=this.pos.y + speed;
          if (this.pos.y>790){
             walking = true;
          }
    }
  
     
}


class Fallschirm extends FigurImpl implements Movement{
  
  void zeich(Figur fig) {
    stroke(255);
    fill(204);
    arc(fig.pos.x, fig.pos.y - 50, 70, 70, PI, TWO_PI);
    line(fig.pos.x, fig.pos.y, fig.pos.x - 35, fig.pos.y - 50 );
    line(fig.pos.x, fig.pos.y, fig.pos.x + 35, fig.pos.y - 50 );
  }
  
  void folgen(Figur fig) {
    float diffX = fig.pos.x - this.pos.x;
    float diffY = fig.pos.y - this.pos.y;
    if (sqrt(pow(diffX, 2) + pow(diffY, 2)) > 50) {
      this.pos.x += diffX / 15;
      this.pos.y += diffY / 15;
    }
  }
  
  void laufen(){}
  void fallen(){}
}
    
