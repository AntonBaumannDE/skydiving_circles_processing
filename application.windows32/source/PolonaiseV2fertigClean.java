import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class PolonaiseV2fertigClean extends PApplet {

Polonaise polonaise;

public void settings() {
  size(1400, 800);
}

public void setup() { 
  polonaise = new Polonaise();
}

public void draw() {
  background(0);
  polonaise.start();
  noCursor();
}

public void mouseClicked(){
        polonaise.a = polonaise.a + 1;
      }
public abstract class Figur {
  PVector pos;
  public abstract void zeich(Figur fig);
}

public abstract class FigurImpl extends Figur {
  FigurImpl() {
    pos = new PVector(700, 700);
  }
  public abstract void zeich(Figur fig);
}

class Maus extends FigurImpl {
  public void zeich(Figur fig) {
    pos.x = mouseX;
    pos.y = mouseY;
    triangle(pos.x-10, pos.y-10, pos.x, pos.y+10, pos.x+10, pos.y-10);
  }
  
}

class Kreis extends FigurImpl implements Movement {
  boolean falling;
  boolean walking;
  boolean folgen;
  float speed = 1.5f;
  int n = 0;
  public void zeich(Figur fig) {
    stroke(255);
    noFill();
    ellipse(this.pos.x, this.pos.y, 20, 20);
    line(this.pos.x, this.pos.y, fig.pos.x, fig.pos.y );
  }
  
  public void folgen(Figur fig) {
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
  
  public void laufen(){
    
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
  
  public void fallen(){

          n++;
          if(n<40){
            this.pos.x=this.pos.x + 0.8f;
          } else if(n<80) {
            this.pos.x=this.pos.x - 0.7f; 
          } else { n = n - 80;}
          this.pos.y=this.pos.y + speed;
          if (this.pos.y>790){
             walking = true;
          }
    }
  
     
}


class Fallschirm extends FigurImpl implements Movement{
  
  public void zeich(Figur fig) {
    stroke(255);
    fill(204);
    arc(fig.pos.x, fig.pos.y - 50, 70, 70, PI, TWO_PI);
    line(fig.pos.x, fig.pos.y, fig.pos.x - 35, fig.pos.y - 50 );
    line(fig.pos.x, fig.pos.y, fig.pos.x + 35, fig.pos.y - 50 );
  }
  
  public void folgen(Figur fig) {
    float diffX = fig.pos.x - this.pos.x;
    float diffY = fig.pos.y - this.pos.y;
    if (sqrt(pow(diffX, 2) + pow(diffY, 2)) > 50) {
      this.pos.x += diffX / 15;
      this.pos.y += diffY / 15;
    }
  }
  
  public void laufen(){}
  public void fallen(){}
}
    
interface Movement {
  public void folgen(Figur fig);
  public void fallen();
  public void laufen();
}
public class Polonaise {

  private Maus maus;
  private Kreis figKreis;
  private Kreis fallKreis;
  private Fallschirm figSchirm;
  private int k = 4;                                                         //Anzahl an Kreisen in der Polonaise
  private StringList kreisNamen = new StringList();                          //-
  private ArrayList<Kreis> kreisListe = new ArrayList<Kreis>();              //Array für Kreise in Polonaise
  private ArrayList<Kreis> fallListe = new ArrayList<Kreis>();               //Array für Kreise im Fall
  private ArrayList<Kreis> walkListe = new ArrayList<Kreis>();               //Array für laufende Kreise
  private ArrayList<Fallschirm> schirmListe = new ArrayList<Fallschirm>();   //Array für Fallschirme
  private boolean fallentrue;
  private boolean walkingtrue;
  private boolean folgentrue;
  private int a = 0;                                                         //a für Adder aktiv
  private int h = 0;                                                         //h als Hilfsmerker für den Adder
  

 

  Polonaise() {
    
    maus = new Maus();
    
    for (int i=0; i<k; i++) {
      kreisNamen.append("figKreis"+i);
      System.out.println(kreisNamen.get(i));
    }
    
    for(int i=0; i<k; i++) {
      figKreis = new Kreis();
      figSchirm = new Fallschirm(); 
      kreisListe.add(figKreis);
      
    }
    
    folgentrue = true;
    
  }
  
  public void start() {

    maus.zeich(maus); 
    
    //FOLGEN
    for(int i=0; i<k; i++){
      int nkreise = kreisListe.size();
      System.out.println(nkreise + "Kreise"); 
      if(kreisListe.get(i).falling==false){
       if (i==0){
        kreisListe.get(i).folgen(maus); //erster Kreis folgt der Maus
        kreisListe.get(i).zeich(maus); 
      } else { 
        kreisListe.get(i).folgen(kreisListe.get(i-1)); //die restlichen Kreise folgen den Vorgängern
        kreisListe.get(i).zeich(kreisListe.get(i-1));
        }
      }
      
      if(kreisListe.get(i).falling==true){
        k = k-1;
        kreisListe.get(i).falling=false;
        fallentrue = true;
        fallListe.add(kreisListe.remove(i));
        schirmListe.add(figSchirm);
        System.out.println("Kreis Nummer " + i +" aus Polonaise ausgetragen und Fallschirm erstellt");
        }             
      }//for closed
    
    
    //FALLEN
    if(fallentrue==true){
      int f = fallListe.size();
      System.out.println(f + "Fallschirmspringer");
      for(int c = 0; c<f; c++){
        if(fallListe.get(c).walking==false){
          System.out.println("Fallschirmspringer " + c + " an Position y: " + fallListe.get(c).pos.y);
          fallListe.get(c).fallen();
          fallListe.get(c).zeich(fallListe.get(c));
          schirmListe.get(c).folgen(fallListe.get(c));
          schirmListe.get(c).zeich(fallListe.get(c));
        }
        if(fallListe.get(c).walking==true){
          f = f - 1;
          fallListe.get(c).walking=false;
          walkingtrue = true;
          walkListe.add(fallListe.remove(c));
          System.out.println("Fallschirmspringer Nummer " + c +" erfolgreich gelandet und nun Läufer");        
        }
      }
    }
    
    //LAUFEN
    if(walkingtrue==true){
      int w = walkListe.size();
      System.out.println(w + "Läufer");
      for(int l = 0; l<w; l++){
        if(walkListe.get(l).folgen==false){
          System.out.println("Läufer " + l + " an Position x: " + walkListe.get(l).pos.x);
          walkListe.get(l).laufen();
          walkListe.get(l).zeich(walkListe.get(l));
        }
        if(walkListe.get(l).folgen==true){
          w = w - 1;
          walkListe.get(l).folgen = false;
          folgentrue = true;
          walkListe.get(l).falling = false;
          kreisListe.add(walkListe.remove(l));
          k = k + 1;
          System.out.println("Läufer Nummer " + l +" hängt sich wieder an die Polonaise");
        }
      }
    }
    
    //KREIS HINZUFÜGEN
    if(a > h){
      figKreis = new Kreis();
      if(maus.pos.x<700){
        figKreis.pos.y=790;
        figKreis.pos.x=10;
      } else {
        figKreis.pos.y=790;
        figKreis.pos.x=1390;
      }       
      kreisListe.add(figKreis);
      k = k + 1;
      h = h + 1;
    }
    
    
 
    
  }    
     
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "PolonaiseV2fertigClean" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
