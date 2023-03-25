import java.util.ArrayList;

class HillClimbing {

    public static Solution hillClimbingWithRestart(IFactory f, int nbRestart) {
        //prérequis : nbRestart >= 1
        //effectue nbRestart fois l'algorithme de hillClimbing, en partant à chaque fois d'un élément donné par f

        //à compléter
        Solution bestSolution = null;
        int bestValue = Integer.MIN_VALUE;

        for (int i = 0; i < nbRestart; i++) {
            IElemHC initialElem = f.getRandomSol();
            Solution currentSolution = initialElem.getSol();
            int currentValue = initialElem.getVal();

            IElemHC bestNeighbor;
            boolean foundBetterNeighbor;
            do {
                foundBetterNeighbor = false;
                ArrayList<? extends IElemHC> neighbors = initialElem.getVoisins();
                bestNeighbor = null;

                for (IElemHC neighbor : neighbors) {
                    int neighborValue = neighbor.getVal();
                    if (neighborValue > currentValue) {
                        bestNeighbor = neighbor;
                        foundBetterNeighbor = true;
                        break;
                    } else if (neighborValue == currentValue && bestNeighbor == null) {
                        bestNeighbor = neighbor;
                    }
                }

                if (foundBetterNeighbor) {
                    initialElem = bestNeighbor;
                    currentSolution = bestNeighbor.getSol();
                    currentValue = bestNeighbor.getVal();
                }

            } while (foundBetterNeighbor);

            if (currentValue > bestValue) {
                bestSolution = currentSolution;
                bestValue = currentValue;
            }
        }

        return bestSolution;
    }

    public static IElemHC hillClimbing(IElemHC s){
        //effectue une recherche locale en partant de s :
        // - en prenant à chaque étape le meilleur des voisins de la solution courante (ou un des meilleurs si il y a plusieurs ex aequo)
        // - en s'arrêtant dès que la solution courante n'a pas de voisin strictement meilleur qu'elle
        // (meilleur au sens de getVal strictement plus grand)

        // à compléter
        Solution currentSolution = s.getSol();
        int currentValue = s.getVal();
        IElemHC bestNeighbor;
        boolean foundBetterNeighbor;

        do {
            foundBetterNeighbor = false;
            ArrayList<ElemPermutHC> neighbors = (ArrayList<ElemPermutHC>) s.getVoisins();
            bestNeighbor = null;

            for (ElemPermutHC neighbor : neighbors) {
                int neighborValue = neighbor.getVal();
                if (neighborValue > currentValue) {
                    bestNeighbor = neighbor;
                    foundBetterNeighbor = true;
                    break;
                } else if (neighborValue == currentValue && bestNeighbor == null) {
                    bestNeighbor = neighbor;
                }
            }

            if (foundBetterNeighbor) {
                currentSolution = bestNeighbor.getSol();
                currentValue = bestNeighbor.getVal();
                s = bestNeighbor;
            }

        } while (foundBetterNeighbor);

        return bestNeighbor;
    }
}