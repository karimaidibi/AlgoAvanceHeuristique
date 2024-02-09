import java.util.*;

public class Instance {
    private Coord startingP; //point de départ
    private int k; //k>=0, nombre de pas disponibles
    private boolean[][] plateau; //orientation: plateau[0][0] en haut à gauche, et plateau[ligne][col]

    //sortie du problème: une Solution (une solution est valide ssi c'est une liste de coordonées de taille au plus k+1, tel que deux coordonnées consécutives sont à distance 1,
    // et les coordonnées ne sortent pas du plateau, et que la solution démarre bien par le point de départ)


    private ArrayList<Coord> listeCoordPieces;// attribut supplémentaire (qui est certes redondant) qui contiendra la liste des coordonnées des pièces du plateau
    //on numérote les pièces de haut en bas, puis de gauche à droite, par exemple sur l'instance suivante (s représente
    //le startinP et x les pièces
    //.x..x
    //x..s.
    //....x

    //les numéros sont
    //.0..1
    //2..s.
    //....3
    //et donc listeCoordPices.get(0) est la Coord (0,1)



    /************************************************
     **** debut methodes fournies              ******
     *************************************************/
    public Instance(boolean[][] p, Coord s, int kk, int hh) {
        plateau = p;
        startingP = s;
        k = kk;
        listeCoordPieces = getListeCoordPieces();
    }

    public Instance(boolean[][] p, Coord s, int kk) {
        plateau = p;
        startingP = s;
        k = kk;
        listeCoordPieces = getListeCoordPieces();
    }


   public Instance(Instance i){ //créer une instance qui est une deep copy (this doit etre independante de i)
        boolean [][] p2 = new boolean[i.plateau.length][i.plateau[0].length];
        for(int l = 0;l < p2.length;l++) {
            for (int c = 0; c < p2[0].length; c++) {
                p2[l][c] = i.plateau[l][c];
            }
        }

        plateau=p2;
        startingP=new Coord(i.startingP);
        k = i.k;
        listeCoordPieces = new ArrayList<>();
        for(Coord c : i.listeCoordPieces){
            listeCoordPieces.add(new Coord(c) );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instance)) return false;
        Instance instance = (Instance) o;
        return getK() == instance.getK() && getStartingP().equals(instance.getStartingP()) && Algos.egalEnsembliste(getListeCoordPieces(),instance.getListeCoordPieces());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartingP(), getK(), getListeCoordPieces());
    }

    public int getNbL() {
        return plateau.length;
    }

    public int getNbC() {
        return plateau[0].length;
    }

    public Coord getStartingP() {
        return startingP;
    }

    public void setStartingP(Coord c) {
        startingP=c;
    }

    public int getK() {
        return k;
    }

    public void setK(int kk) {
        k=kk;
    }


    public ArrayList<Coord> getListeCoordPieces() {
        if(listeCoordPieces==null){
        ArrayList<Coord> listeCoordPieces = new ArrayList<>();
        for (int l = 0; l < getNbL(); l++) {
            for (int c = 0; c < getNbC(); c++) {
                if (piecePresente(new Coord(l, c))) {
                    listeCoordPieces.add(new Coord(l, c));
                }
            }
        }
        return listeCoordPieces;}
        else
            return listeCoordPieces;
    }


    public boolean piecePresente(Coord c) {
        return plateau[c.getL()][c.getC()];
    }

    public void retirerPiece(Coord c){
        //si pas de piece en c ne fait rien, sinon la retire du plateau et met à jour les coordonnées
        if(piecePresente(c)){
            plateau[c.getL()][c.getC()] =false;
            listeCoordPieces.remove(c);
        }
    }


    @Override
    public String toString() {
       //retourne une chaine représentant this,
        StringBuilder res = new StringBuilder("k = " + k + "\n" + "nb pieces = " + getListeCoordPieces().size() + "\nstarting point = " + startingP + "\n");
        for (int l = 0; l < getNbL(); l++) {
            for (int c = 0; c < getNbC(); c++) {

                if (startingP.equals(new Coord(l,c)))
                    res.append("s");
                else{
                    if (piecePresente(new Coord(l, c))) {
                        res.append("x");
                    } else {
                        res.append(".");
                    }
                }

            }
            res.append("\n");
        }
        return res.toString()+ "\nliste pieces " + getListeCoordPieces();
    }

    public String toString(Solution s) {

        //retourne une chaine sous la forme suivante
        //o!..
        //.ox.
        //.o..
        //.o..

        //où
        // '.' signifie que la solution ne passe pas là, et qu'il n'y a pas de pièce
        // 'x' signifie que la solution ne passe pas là, et qu'il y a une pièce
        // 'o' signifie que la solution passe par là, et qu'il n'y a pas de pièce
        // '!' signifie que la solution passe par là, et qu'il y a une pièce

        // dans l'exemple ci-dessus, on avait donc 2 pièces dans l'instance (dont 1 ramassée par s)
        //et la chaîne de l'exemple contient 4 fois le caractère "\n" (une fois à chaque fin de ligne)

        if(s==null) return null;
        StringBuilder res = new StringBuilder("");//\n k = " + k + "\n" + "nb pieces = " + listeCoordPieces.size() + "\n");
        for (int l = 0; l < getNbL(); l++) {
            for (int c = 0; c < getNbC(); c++) {
                if (startingP.equals(new Coord(l,c)))
                    res.append("s");
                else {
                    if (s.contains(new Coord(l, c)) && piecePresente(new Coord(l, c))) {
                        res.append("!");
                    }
                    if (!s.contains(new Coord(l, c)) && piecePresente(new Coord(l, c))) {
                        res.append("x");
                    }
                    if (s.contains(new Coord(l, c)) && !piecePresente(new Coord(l, c))) {
                        res.append("o");
                    }
                    if (!s.contains(new Coord(l, c)) && !piecePresente(new Coord(l, c))) {
                        res.append(".");
                    }
                }
            }
            res.append("\n");
        }
        return res.toString();
    }
    /************************************************
     **** méthodes à fournir relatives à une solution **
     *************************************************/



    public boolean estValide(Solution s) {
        //prérequis : s!=null, et les s.get(i) !=null pour tout i (mais par contre s peut contenir n'importe quelle séquence de coordonnées)
        //retourne vrai ssi s est une solution valide (une solution est valide ssi c'est une liste de coordonnées de taille au plus k+1, telle que deux coordonnées consécutives sont à distance 1,
        // et les coordonnées ne sortent pas du plateau, et que la solution démarre bien par le point de départ)

        if (s == null || s.size() > k + 1) { // Vérifie si la solution est null ou si la taille de la solution est supérieure à k + 1
            return false;
        }

        if (!s.get(0).equals(startingP)) { // Vérifie si la première coordonnée est égale à la coordonnée de départ
            return false;
        }

        for (int i = 1; i < s.size(); i++) { // Parcourt les coordonnées de la solution
            Coord current = s.get(i);
            Coord previous = s.get(i - 1);

            // Vérifie si les coordonnées sont à l'intérieur du plateau
            if (!current.estDansPlateau(this.plateau.length, this.plateau[0].length)) {
                return false;
            }

            // Vérifie si les coordonnées consécutives sont à distance 1
            if (!current.estADistanceUn(previous)) {
                return false;
            }
        }

        return true; // Si toutes les conditions sont remplies, retourne vrai
    }


    public int evaluerSolution(Solution s) {
        //prerequis : s est valide (et donc !=null)
        //action : retourne le nombre de pièces ramassées par s (et ne doit pas modifier this ni s)

        int count = 0; // Initialise le compteur de pièces ramassées
        if (s != null) { // Si il y'a une solution, parcourir ces coordonnées
            for (Coord coord : s) { // Parcourt les coordonnées de la solution
                if (piecePresente(coord)) { // Vérifie si une pièce est présente à cette coordonnée
                    count++; // Incrémente le compteur de pièces ramassées
                }
            }
        }
        return count; // Retourne le nombre de pièces ramassées
    }



    public int nbStepsToCollectAll(ArrayList<Integer> permut) {

        //prérequis : permut est une permutation des entiers {0,..,listeCoordPieces.size()-1}
        // (et donc permut peut être vide, mais pas null, si il n'y a pas de pièces)

        //retourne le nombre de pas qu'il faudrait pour ramasser toutes les pièces dans l'ordre de permut

        //à compléter
        int nbSteps = 0;
        Coord currentPos = new Coord(getStartingP().getL(), getStartingP().getC());
        for (int i = 0; i < permut.size(); i++) {
            Coord piecePos = getListeCoordPieces().get(permut.get(i));
            nbSteps += currentPos.distanceFrom(piecePos);
            currentPos = piecePos;
        }
        //nbSteps += currentPos.distanceFrom(getStartingP());
        return nbSteps;
    }

    /************************************************
     **** méthodes à fournir relatives au greedy        ******
     *************************************************/



    public ArrayList<Integer> greedyPermut() {
        //retourne une liste (x1,..,xp) où
        //x1 est la pièce la plus proche du point de départ
        //x2 est la pièce restante la plus proche de x1
        //x3 est la pièce restante la plus proche de x2
        //etc
        //Remarques :
        // -on doit donc retourner une sequence de taille listeCoordPieces.size() (donc sequence vide (et pas null) si il n'y a pas de pièces)
        // -si à un moment donné, lorsque l'on est sur une pièce xi, la pièce restante la plus proche de xi n'est pas unique,
        //   alors on peut prendre n'importe quelle pièce (parmi ces plus proches de xi)
        //par exemple,
        //si le plateau est
        //.s.x
        //....
        //x..x
        //avec la pièce 0 en haut à droite, la pièce 1 en bas à gauche, et la pièce 2 en bas à droite,
        //on doit retourner (0,2,1)

        //a compléter

        ArrayList<Integer> permut = new ArrayList<>();
        ArrayList<Coord> remainingPieces = new ArrayList<>(listeCoordPieces);

        Coord currentPosition = startingP;

        while (!remainingPieces.isEmpty()) {
            double minDistance = Double.MAX_VALUE;
            Coord closestPiece = null;
            int closestPieceIndex = -1;

            for (int i = 0; i < remainingPieces.size(); i++) {
                Coord piece = remainingPieces.get(i);
                double distance = currentPosition.distanceFrom(piece);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPiece = piece;
                    closestPieceIndex = i;
                }
            }

            permut.add(listeCoordPieces.indexOf(closestPiece));
            remainingPieces.remove(closestPieceIndex);
            currentPosition = closestPiece;
        }

        return permut;
    }


    public Solution calculerSol(ArrayList<Integer> permut) {

        //prérequis : permut est une permutation des entiers {0,..,listeCoordPieces.size()-1}
        // (et donc permut peut être vide, mais pas null, si il n'y a pas de pièces)

        //retourne la solution obtenue en concaténant les plus courts chemins successifs pour attraper
        // les pièces dans l'ordre donné par this.permut, jusqu'à avoir k mouvements ou à avoir ramassé toutes les pièces de la permut.
        // Remarque : entre deux pièces consécutives, le plus court chemin n'est pas unique, n'importe quel plus court chemin est ok.
        //par ex, si le plateau est
        //.s.x
        //....
        //x..x
        //avec la pièce 0 en haut à droite, la pièce 1 en bas à gauche, et la pièce 2 en bas à droite,
        //et que permut = (0,2,1), alors pour
        //k=3, il faut retourner (0,1)(0,2)(0,3)(1,3)  (dans ce cas là les plus courts sont uniques)
        //k=10, il faut retourner (0,1)(0,2)(0,3)(1,3)(2,3)(2,2)(2,1)(2,0)  (dans ce cas là les plus courts sont aussi uniques,
        // et on s'arrête avant d'avoir fait k pas car on a tout collecté)

        //a compléter

        // Si la permutation est vide, retourne une solution vide
        if (permut == null || permut.isEmpty()) {
            return new Solution();
        }

        //nouvelle solution
        ArrayList<Coord> solution = new Solution();
        Coord currentPosition = this.startingP;
        int k = 0; // nombre de pas
        ArrayList<Integer> piecesRamasse = new ArrayList<>();
        boolean toutesPiecesRamasse = false;

        solution.add(currentPosition);

        for(int i = 0; i<permut.size(); i++){
            Coord piece = listeCoordPieces.get(permut.get(i));
            Boolean sameCoordinate = (currentPosition.getL() == piece.getL()) && (currentPosition.getC() == piece.getC());
            while( (k<this.getK()) && !toutesPiecesRamasse && sameCoordinate==false ){
                currentPosition = this.getNextStepTowards(currentPosition, piece);
                k++;
                solution.add(currentPosition);
                sameCoordinate = (currentPosition.getL() == piece.getL()) && (currentPosition.getC() == piece.getC());
            }
            if(sameCoordinate){
                piecesRamasse.add(listeCoordPieces.indexOf(piece));
                toutesPiecesRamasse = piecesRamasse.containsAll(permut);
            }
        }
        return (Solution) solution;
    }

    public Coord getNextStepTowards(Coord currentPosition, Coord closestPosition) {
        int currentRow = currentPosition.getL();
        int currentCol = currentPosition.getC();
        int closestRow = closestPosition.getL();
        int closestCol = closestPosition.getC();

        int newRow = currentRow;
        int newCol = currentCol;

        if (currentRow < closestRow) {
            newRow = currentRow + 1;
        } else if (currentRow > closestRow) {
            newRow = currentRow - 1;
        } else if (currentCol < closestCol) {
            newCol = currentCol + 1;
        } else if (currentCol > closestCol) {
            newCol = currentCol - 1;
        }

        return new Coord(newRow, newCol);
    }


    List<Coord> getValidNeighbors(Coord coord) {
        List<Coord> neighbors = new ArrayList<>();
        int[] rowMoves = {-1, 0, 1, 0};
        int[] colMoves = {0, 1, 0, -1};

        for (int i = 0; i < 4; i++) {
            int newRow = coord.getL() + rowMoves[i];
            int newCol = coord.getC() + colMoves[i];

            if (isValid(newRow, newCol)) {
                neighbors.add(new Coord(newRow, newCol));
            }
        }

        return neighbors;
    }

    private boolean isValid(int row, int col) {
        int numRows = plateau.length;
        int numCols = plateau[0].length;

        return row >= 0 && row < numRows && col >= 0 && col < numCols && plateau[row][col];
    }

    /************************************************
     **** fin algo algo greedy                      ******
     *************************************************/



    public int borneSup(){
        //soit d0 la distance min entre la position de départ et une pièce
        //soit {d1,..,dx} l'ensemble des distances entre pièces (donc x = (nbpiece-1)*npbpiece / 2), triées par ordre croissant
        //retourne le nombre de pièces que l'on capturerait avec k pas dans le cas idéal où
        //toutes les pièces seraient disposées sur une ligne ainsi : (avec sp le point de départ à gauche)
        //sp .... p .. p .... p ....... p ...
        //    d0    d1    d2      d3
        //(vous pouvez réfléchir au fait que c'est bien une borne supérieure)
        //(pour des exemples précis, cf les tests)

        //à compléter
        int d0 = Integer.MAX_VALUE;
        List<Integer> distances = new ArrayList<>();

        for (Coord piece : this.getListeCoordPieces()) {
            int distFromStart = this.startingP.distanceFrom(piece);
            d0 = Math.min(d0, distFromStart);

            for (Coord otherPiece : this.getListeCoordPieces()) {
                if (!piece.equals(otherPiece)) {
                    int dist = piece.distanceFrom(otherPiece);
                    distances.add(dist);
                }
            }
        }

        Collections.sort(distances);

        int totalDistance = d0;
        int piecesCollected = 1;
        for (int distance : distances) {
            totalDistance += distance;
            if (totalDistance <= this.k) {
                piecesCollected++;
            } else {
                break;
            }
        }

        return piecesCollected;
    }
}
