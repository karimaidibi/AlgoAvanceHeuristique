import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class ElemPermutHC implements IElemHC {

    private Instance i;
    private ArrayList<Integer> permut; //permutation de {0,..,i.getListePieces().size()-1} représentant l'ordre dans lequel on souhaite ramasser les pièces
    private static int dist = 1; //distance à laquelle on génère voisinage

    public ElemPermutHC(Instance i, ArrayList<Integer> p){
        this.i = i;
        permut = p;
    }

    public ElemPermutHC(ElemPermutHC s){
        this.i = new Instance(s.i);
        this.permut = new ArrayList<Integer>();
        permut.addAll(s.permut);
    }

    public static void setDist(int d){
        dist = d;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElemPermutHC)) return false;
        ElemPermutHC that = (ElemPermutHC) o;
        return i.equals(that.i) && permut.equals(that.permut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, permut);
    }

    public int getVal(){


        //retourne nbCases * valSol - nbStepsTotal, où :
        //- nbCases est le nombre de cases du plateau
        //- valSol est la valeur de la solution associée à this
        //- nbStepsTotal est le nombre de pas total qu'il faudrait pour ramasser toutes les pièces dans l'ordre de permut

        // à compléter
        int nbL = i.getNbL();
        int nbC = i.getNbC();
        int nbCases = nbL * nbC;
        Solution sol = i.calculerSol(permut);
        int valSol = i.evaluerSolution(sol);
        int nbStepsTotal = i.nbStepsToCollectAll(permut);
        return nbCases * valSol - nbStepsTotal;
    }

    public Solution getSol(){
        return i.calculerSol(permut);
    }


    public ArrayList<ElemPermutHC> getVoisinsImmediats() {


        //retourne l'ensemble des voisins à dist <= 1 (et donc this fait partie du résultat car à distance 0)
        //voisins = toutes les permutations que l'on peut atteindre en repoussant un élément de permut à la fin
        //ex pour permut = (0,1,2), doit retourner {(1,2,0),(0,2,1),(0,1,2)}
        //les objets retournés doivent être indépendant de this, et cette méthode ne doit pas modifier this

        //ne dois pas modifier this

        //à compléter
        ArrayList<ElemPermutHC> voisins = new ArrayList<>();
        for (int i = 0; i < permut.size(); i++) {
            for (int j = i+1; j < permut.size(); j++) {
                ArrayList<Integer> newPermut = new ArrayList<>(permut);
                newPermut.remove(j);
                newPermut.add(i, permut.get(j));
                voisins.add(new ElemPermutHC(this.i, newPermut));
            }
        }
        return voisins;
    }



    public ArrayList<ElemPermutHC> getVoisins(){

        //retourne voisins (sans doublons) à une distance <= dist
        //pour dist = 1, doit retourner getVoisinsImmediats();

        //à compléter
        HashSet<ElemPermutHC> voisins = new HashSet<>(getVoisinsImmediats());
        ArrayList<ElemPermutHC> result = new ArrayList<>(voisins);
        for (int i = 1; i < dist; i++) {
            ArrayList<ElemPermutHC> temp = new ArrayList<>(result);
            for (ElemPermutHC elem : temp) {
                result.addAll(elem.getVoisinsImmediats());
            }
        }
        return result;
    }

}