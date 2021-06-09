/*************************************************************************
 *  Compilation:  javac ArtCollage.java
 *  Execution:    java ArtCollage Flo2.jpeg
 *
 *  @author: Aryan Patel - adp182 - adp182@scarletmail.rutgers.edu
 *
 *************************************************************************/

import java.awt.Color;


public class ArtCollage {

    // The orginal picture
    private Picture original;

    // The collage picture
    private Picture collage;

    // The collage Picture consists of collageDimension X collageDimension tiles
    private int collageDimension;

    // A tile consists of tileDimension X tileDimension pixels
    private int tileDimension;
    
    /*
     * One-argument Constructor
     * 1. set default values of collageDimension to 4 and tileDimension to 100
     * 2. initializes original with the filename image
     * 3. initializes collage as a Picture of tileDimension*collageDimension x tileDimension*collageDimension, 
     *    where each pixel is black (see all constructors for the Picture class).
     * 4. update collage to be a scaled version of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage (String filename) {

        this.collageDimension = 4;
        this.tileDimension = 100;

        int w = collageDimension*tileDimension;
        int h = collageDimension*tileDimension;
        
        this.original = new Picture(filename);
        this.collage = new Picture(this.collageDimension*this.tileDimension,
            this.collageDimension*this.tileDimension);

        for(int t_col = 0; t_col < w; t_col++){
            for(int t_row = 0; t_row < h; t_row++){
                
                int s_col = t_col* original.width() /w;
                int s_row = t_row* original.height() /h;

                Color color = original.get(s_col, s_row);
                collage.set(t_col, t_row, color);
            }
        }

    }

    /*
     * Three-arguments Constructor
     * 1. set default values of collageDimension to cd and tileDimension to td
     * 2. initializes original with the filename image
     * 3. initializes collage as a Picture of tileDimension*collageDimension x tileDimension*collageDimension, 
     *    where each pixel is black (see all constructors for the Picture class).
     * 4. update collage to be a scaled version of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage (String filename, int td, int cd) {

        this.tileDimension = td;
        this.collageDimension = cd;

        int w = collageDimension*tileDimension;
        int h = collageDimension*tileDimension;

        this.original = new Picture(filename);
        this.collage = new Picture(this.collageDimension*this.tileDimension,
            this.collageDimension*this.tileDimension);

        for(int t_col = 0; t_col < w; t_col++){
            for(int t_row = 0; t_row < h; t_row++){
                
                int s_col = t_col* original.width() /w;
                int s_row = t_row* original.height() /h;
    
                Color color = original.get(s_col, s_row);
                collage.set(t_col, t_row, color);
            }
        }

    }

    /*
     * Returns the collageDimension instance variable
     *
     * @return collageDimension
     */
    public int getCollageDimension() {

        return collageDimension;
    }

    /*
     * Returns the tileDimension instance variable
     *
     * @return tileDimension
     */
    public int getTileDimension() {

        return tileDimension;
    }

    /*
     * Returns original instance variable
     *
     * @return original
     */
    public Picture getOriginalPicture() {

        return original;
    }

    /*
     * Returns collage instance variable
     *
     * @return collage
     */
    public Picture getCollagePicture() {

        return collage;
    }
    
    /*
     * Display the original image
     * Assumes that original has been initialized
     */
    public void showOriginalPicture() {

        original.show();
    }

    /*
     * Display the collage image
     * Assumes that collage has been initialized
     */
    public void showCollagePicture() {

        collage.show();
    }

    /*
     * Replaces the tile at collageCol,collageRow with the image from filename
     * Tile (0,0) is the upper leftmost tile
     *
     * @param filename image to replace tile
     * @param collageCol tile column
     * @param collageRow tile row
     */
    public void replaceTile (String filename,  int collageCol, int collageRow) {

        
        Picture replacement = new Picture(filename);

        for(int c_col = 0; c_col < tileDimension*collageDimension; c_col+=tileDimension){
            for(int c_row = 0; c_row < tileDimension*collageDimension; c_row+=tileDimension){
                
                if(c_col/tileDimension == collageCol && 
                c_row/tileDimension == collageRow){

                    for(int t_col = c_col; t_col < tileDimension+c_col; t_col++){
                        for(int t_row = c_row; t_row < tileDimension+c_row; t_row++){
                        
                            int s_col = (t_col - c_col)* replacement.width() /tileDimension;
                            int s_row = (t_row - c_row)* replacement.height() /tileDimension;
                
                            Color color = replacement.get(s_col, s_row);
                            collage.set(t_col, t_row, color);
                            
                        }
                    }
                }
            }
        }

        
    }
    
    /*
     * Makes a collage of tiles from original Picture
     * original will have collageDimension x collageDimension tiles, each tile
     * has tileDimension X tileDimension pixels
     */
    public void makeCollage () {

    
        for(int c_col = 0; c_col < tileDimension*collageDimension; c_col+=tileDimension){
            for(int c_row = 0; c_row < tileDimension*collageDimension; c_row+=tileDimension){
                
                for(int t_col = c_col; t_col < tileDimension+c_col; t_col++){
                    for(int t_row = c_row; t_row < tileDimension+c_row; t_row++){
                        
                        int s_col = (t_col - c_col)* original.width() /tileDimension;
                        int s_row = (t_row - c_row)* original.height() /tileDimension;
            
                        Color color = original.get(s_col, s_row);
                        collage.set(t_col, t_row, color);
                        
                    }
                }
            }
        }


    }

    /*
     * Colorizes the tile at (collageCol, collageRow) with component 
     * (see CS111 Week 9 slides, the code for color separation is at the 
     *  book's website)
     *
     * @param component is either red, blue or green
     * @param collageCol tile column
     * @param collageRow tile row
     */
    public void colorizeTile (String component,  int collageCol, int collageRow) {


        int startCol = (collageCol * tileDimension);
        int startRow = (collageRow * tileDimension);

        
        for(int t_col = startCol; t_col < tileDimension+startCol; t_col++){
            for(int t_row = startRow; t_row < tileDimension+startRow; t_row++){

                Color color = collage.get(t_col, t_row);

                if(component == "red"){
                    collage.set(t_col, t_row, new Color(color.getRed(),0,0));
                } else if (component == "green"){
                    collage.set(t_col, t_row, new Color(0,color.getGreen(),0));
                } else if (component == "blue"){
                    collage.set(t_col, t_row, new Color(0,0,color.getBlue()));
                }

                
            }
        }


    }

    /*
     * Grayscale tile at (collageCol, collageRow)
     * (see CS111 Week 9 slides, the code for luminance is at the book's website)
     *
     * @param collageCol tile column
     * @param collageRow tile row
     */

    public void grayscaleTile (int collageCol, int collageRow) {


        int startCol = (collageCol * tileDimension);
        int startRow = (collageRow * tileDimension);

        
        for(int t_col = startCol; t_col < tileDimension+startCol; t_col++){
            for(int t_row = startRow; t_row < tileDimension+startRow; t_row++){

                Color color = collage.get(t_col, t_row);

                Color gray = Luminance.toGray(color);
                collage.set(t_col, t_row, gray);
                
            }
        }


    }


    /*
     *
     *  Test client: use the examples given on the assignment description to test your ArtCollage
     */
    public static void main (String[] args) {

        ArtCollage art = new ArtCollage(args[0], 200, 4);
        art.makeCollage();

        // Replace 3 tiles 
        //art.replaceTile("Baloo.jpeg",0,1);
        //art.replaceTile("Flo2.jpeg",1,0);
        //art.replaceTile("PlocLilo.jpg",1,1);

        art.colorizeTile("red", 2, 2);
        art.grayscaleTile(1, 1);
        art.colorizeTile("green", 0, 0);
        art.colorizeTile("blue", 3, 1);
        
        art.showCollagePicture();
                
    }
}