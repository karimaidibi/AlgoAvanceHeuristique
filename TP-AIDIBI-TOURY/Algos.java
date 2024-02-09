

import java.util.*;

public class Algos {

    public static boolean egalEnsembliste(ArrayList<?> a1, ArrayList<?> a2){
        //retourn vrai ssi les a1 à les même éléments que a2 (peut importe l'ordre)
        return a1.containsAll(a2) && a2.containsAll(a1);
    }


    public static Solution greedySolver(Instance i) {

        //calcule la solution obtenue en allant chercher à chaque étape la pièce restante la plus proche
        //(si plusieurs pièces sont à la même distance, on fait un choix arbitraire)

        return i.calculerSol(i.greedyPermut());
    }


    public static Solution algoFPT1(InstanceDec id) {
        //algorithme qui décide id (c'est à dire si opt(id.i) >= id.c) en branchant (en 4^k) dans les 4 directions pour chacun des k pas
        //retourne une solution de valeur >= c si une telle solution existe, et null sinon
        //Ne doit pas modifier le paramètre
        //Rappel : si c==0, on peut retourner la solution égale au point de départ puisque l'on est pas obligé d'utiliser les k pas
        // (on peut aussi retourner une solution plus longue si on veut)
        //Remarque : quand vous aurez codé la borneSup, pensez à l'utiliser dans cet algorithme pour ajouter un cas de base

        //à compléter
        // Si c == 0, on peut retourner la solution égale au point de départ
        if (id.c == 0) {
            return new Solution(id.i.getStartingP());
        }

        // Si K == 0, cad on ne peut pas bouger, retourner vrai si le nombre de piece a ramasser est 1 et que la position de depart contienr une piece
        if(id.i.getK()==0){
            if(id.c == 1 && id.i.piecePresente(id.i.getStartingP())){
                return new Solution(id.i.getStartingP());
            }else{
                return null;
            }
        }

        // Utilisez la méthode borneSup pour vérifier si la solution est réalisable
        int maxCoins = id.i.borneSup();
        if (maxCoins < id.c) {
            return null;
        }

        // Initialisez la solution initiale avec le point de départ
        Solution currentSolution = new Solution(id.i.getStartingP());

        // Appelez la fonction auxiliaire fpt pour explorer toutes les combinaisons possibles de déplacements
        return fpt(id.i, id.c, id.i.getStartingP(), id.i.getK(), currentSolution, 0);

    }

    public static Solution fpt(Instance i, int remainingCoins, Coord currentPos, int remainingSteps, Solution currentSolution, int currentCoins) {
        // Si c == 0, on peut retourner la solution égale au point de départ
        if(remainingCoins==0){
            return new Solution(currentPos);
        }

        // si on a ramasse plus de piece que le nombre de piece a ramasser
        if (remainingCoins <= currentCoins) {
            return currentSolution;
        }

        // Si K == 0, cad on ne peut pas bouger, retourner vrai si le nombre de piece a ramasser est 1 et que la position de depart contienr une piece
        if(remainingSteps == 0){
            if(remainingCoins == 1 && i.piecePresente(currentPos)){
                return new Solution(currentPos);
            }else{
                return null;
            }
        }

        int n = i.getNbL();
        int m = i.getNbC();
        Coord[] directions = new Coord[]{new Coord(-1, 0), new Coord(1, 0), new Coord(0, -1), new Coord(0, 1)};
        Solution bestSolution = null;

        for (Coord dir : directions) {
            Coord newPos = new Coord(currentPos.getL() + dir.getL(), currentPos.getC() + dir.getC());
            if (newPos.estDansPlateau(n, m)) {
                Solution newSolution = cloneSolution(currentSolution);
                newSolution.add(newPos);
                int newCoins = currentCoins;
                if (i.getListeCoordPieces().contains(newPos) && !currentSolution.contains(newPos)) {
                    newCoins++;
                }
                Solution foundSolution = fpt(i, remainingCoins, newPos, remainingSteps - 1, newSolution, newCoins);
                if (foundSolution != null && (bestSolution == null || foundSolution.size() > bestSolution.size())) {
                    bestSolution = foundSolution;
                }
            }
        }

        return bestSolution;
    }

    public static Solution cloneSolution(Solution original) {
        Solution clonedSolution = new Solution();
        for (Coord coord : original) {
            clonedSolution.addCoord(new Coord(coord));
        }
        return clonedSolution;
    }

    public static Solution algoFPT1DP(InstanceDec id,  HashMap<InstanceDec,Solution> table) {
        //même spécification que algoFPT1, si ce n'est que
        // - si table.containsKey(id), alors id a déjà été calculée, et on se contente de retourner table.get(id)
        // - sinon, alors on doit calculer la solution s pour id, la ranger dans la table (table.put(id,res)), et la retourner

        //Remarques
        // - ne doit pas modifier l'instance id en param (mais va modifier la table bien sûr)
        // - même si le branchement est le même que dans algoFPT1, ne faites PAS appel à algoFPT1 (et donc il y aura de la duplication de code)


        //à compléter
        // Si l'instance est déjà résolue, on retourne la solution correspondante
        if (table.containsKey(id)) {
            return table.get(id);
        }

        // Sinon, on calcule la solution en faisant appel à la méthode auxiliaire ftp
        int k = id.i.getK();
        int c = id.c;
        int n = id.i.getNbL();
        int m = id.i.getNbC();

        Solution sol = fpt(id.i, c, id.i.getStartingP(), k, new Solution(id.i.getStartingP()), 0);

        // On ajoute la solution à la table et on la retourne
        table.put(id, sol);
        return sol;
    }


    public static Solution algoFPT1DPClient(InstanceDec id){
        //si il est possible de collecter >= id.c pièces dans id.i, alors retourne une Solution de valeur >= c, sinon retourne null
        //doit faire appel à algoFPT1DP

        //à completer
        HashMap<InstanceDec, Solution> table = new HashMap<>();
        return algoFPT1DP(id, table);
    }



}
