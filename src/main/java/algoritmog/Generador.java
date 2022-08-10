package algoritmog;

import java.util.Arrays;
import java.util.Random;

public class Generador {


    Population population = new Population();
    Individual fittest;
    Individual secondFittest;
    int generationCount = 0;

    public static void main(String[] args) {

        Random rn = new Random();

        Generador generador = new Generador();

        //Inicializar población
        generador.population.initializePopulation(10);

        //Calcular la aptitud de cada individuo
        generador.population.calculateFitness();

        System.out.println("Ciclo: " + generador.generationCount + " Maxima Aptitud: " + generador.population.fittest);

        //Ciclo para obtener el individuo mas apto
        while (generador.population.fittest < 5) {
            ++generador.generationCount;

            //Seleccionar
            generador.selection();

            //Cruzar
            generador.crossover();

            //Hacer mutación bajo una probabilidad aleatoria
            if (rn.nextInt()%7 < 5) {
                generador.mutation();
            }

            //Añadir descendientes más aptos a la población
            generador.addFittestOffspring();

            //Calcular nuevos valores
            generador.population.calculateFitness();

            System.out.println("Ciclo: " + generador.generationCount + " Maxima Aptitud: " + generador.population.fittest);
        }

        System.out.println("\nSolucion encontrada en Ciclo " + generador.generationCount);
        System.out.println("Maxima Aptitud: "+generador.population.getFittest().fitness);

        System.out.println("Genes del mas Apto"+ Arrays.toString(generador.population.getFittest().genes));

        System.out.println("");

    }

    //Selección
    void selection() {

        //Seleccionar al individuo más apto
        fittest = population.getFittest();

        //Seleccione el segundo individuo más apto
        secondFittest = population.getSecondFittest(fittest.i);
    }

    //Cruce
    void crossover() {
        Random rn = new Random();
        //Seleccione un punto de cruce aleatorio
        int crossOverPoint = rn.nextInt(population.individuals[0].geneLength);


        //Intercambio de valores entre padres
        for (int i = 0; i < crossOverPoint; i++) {
            int temp = fittest.genes[i];
            fittest.genes[i] = secondFittest.genes[i];
            secondFittest.genes[i] = temp;

        }
        System.out.println("Cruzar padres");


    }

    //Mutación
    void mutation() {
        Random rn = new Random();

        //Seleccione un punto de mutación aleatorio
        int mutationPoint = rn.nextInt(population.individuals[0].geneLength);

        //Intercambiar valores
        if (fittest.genes[mutationPoint] == 0) {
            fittest.genes[mutationPoint] = 1;
        } else {
            fittest.genes[mutationPoint] = 0;
        }

        mutationPoint = rn.nextInt(population.individuals[0].geneLength);

        if (secondFittest.genes[mutationPoint] == 0) {
            secondFittest.genes[mutationPoint] = 1;
        } else {
            secondFittest.genes[mutationPoint] = 0;
        }
        System.out.println("Mutacion aleatoria");

    }

    //Obtener el descendiente mas apto
    Individual getFittestOffspring() {
        if (fittest.fitness > secondFittest.fitness) {
            return fittest;
        }
        return secondFittest;
    }


    //Reemplazar al individuo menos apto de los descendientes más aptos
    void addFittestOffspring() {

        //Actualizar los valores de Aptitud de la descendencia
        fittest.calcFitness();
        secondFittest.calcFitness();

        //Obtener el índice del individuo menos apto
        int leastFittestIndex = population.getLeastFittestIndex();

        //Reemplazar al individuo menos apto de los descendientes más aptos
        population.individuals[leastFittestIndex] = getFittestOffspring();
    }

}


//Individual class
class Individual implements Cloneable{

    int fitness = 0;
    int[] genes = new int[5];
    int geneLength = 5;
    int i = 0;
    public Individual() {
        Random rn = new Random();

        //Establecer genes al azar para cada individuo
        for (int i = 0; i < genes.length; i++) {
            genes[i] = Math.abs(rn.nextInt() % 2);
        }

        fitness = 0;
    }

    //Calculate fitness
    public void calcFitness() {

        fitness = 0;
        for (int i = 0; i < 5; i++) {
            if (genes[i] == 1) {
                ++fitness;
            }
        }
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        Individual individual = (Individual)super.clone();
        individual.genes = new int[5];
        for(int i = 0; i < individual.genes.length; i++){
            individual.genes[i] = this.genes[i];
        }
        return individual;
    }
}

//Population class
class Population {

    int popSize = 10;
    Individual[] individuals = new Individual[10];
    int fittest = 0;

    //Inicializar poblacion
    public void initializePopulation(int size) {
        System.out.println("Inicializacion de Poblacion");

        for (int i = 0; i < individuals.length; i++) {
            individuals[i] = new Individual();
            individuals[i].i = i;
            System.out.println("Individuo "+i+" Cromosomas: "+ Arrays.toString(individuals[i].genes));

        }

    }

    //Get the fittest individual
    public Individual getFittest() {
        int maxFit = Integer.MIN_VALUE;
        int maxFitIndex = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (maxFit <= individuals[i].fitness) {
                maxFit = individuals[i].fitness;
                maxFitIndex = i;
            }
        }
        fittest = individuals[maxFitIndex].fitness;
        System.out.println("Individuo "+maxFitIndex+" con mayor Aptitud Cromosomas: "+ Arrays.toString(individuals[maxFitIndex].genes));

        try {
            return (Individual) individuals[maxFitIndex].clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Get the second most fittest individual
    public Individual getSecondFittest(int j) {
        int maxFit = Integer.MIN_VALUE;
        int maxFitIndex = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (maxFit <= individuals[i].fitness && i!=j) {
                maxFit = individuals[i].fitness;
                maxFitIndex = i;
            }
        }
        System.out.println("Individuo "+maxFit+" con segunda mayor Aptitud Cromosomas: "+ Arrays.toString(individuals[maxFit].genes));

        try {
            return (Individual) individuals[maxFit].clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Get index of least fittest individual
    public int getLeastFittestIndex() {
        int minFitVal = Integer.MAX_VALUE;
        int minFitIndex = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (minFitVal >= individuals[i].fitness) {
                minFitVal = individuals[i].fitness;
                minFitIndex = i;
            }
        }
        return minFitIndex;
    }

    //Calculate fitness of each individual
    public void calculateFitness() {
        System.out.println("Calculo de Aptitudes ");

        for (int i = 0; i < individuals.length; i++) {
            individuals[i].calcFitness();
            System.out.println("Individuo "+i+" "+ Arrays.toString(individuals[i].genes) +" Aptitud: "+ individuals[i].fitness);
        }
        getFittest();
    }

}
