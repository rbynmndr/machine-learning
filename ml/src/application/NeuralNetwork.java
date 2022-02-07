package application;

public class NeuralNetwork {
	private int inputNodes;	//784
	private int hiddenNodes;//100
	private int outputNodes;//10
	private double learningRate;//0.3
	private double[][] wih;
	private double[][] who;
	Matrix matrix = new Matrix();

	public NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes, double learningRate) {
		this.inputNodes = inputNodes;
		this.hiddenNodes = hiddenNodes;
		this.outputNodes = outputNodes;
		this.learningRate = learningRate;
		this.wih = matrix.createRandomMatrix(0, hiddenNodes, inputNodes);	//100 X 784
		this.who = matrix.createRandomMatrix(0, outputNodes, hiddenNodes);	//10 X 100
	}
	
	public void train(double[][] inputLists, double[][] targetLists) { // inputList must be 1 x 784., tragetList must be 1 x 10.
		inputLists = matrix.transpose(inputLists);
		targetLists = matrix.transpose(targetLists);
		
		//calculate signals into hidden layer.
		double[][] hiddenInputs = matrix.dot(this.wih, inputLists);
		double[][] hiddenOutputs = matrix.sigmoidMatrix(hiddenInputs);
		
		//calculate signals into output layer.
		double[][] finalInputs = matrix.dot(this.who, hiddenOutputs);
		double[][] finalOutputs = matrix.sigmoidMatrix(finalInputs);
		
		//output layer error
		double[][] outputError = matrix.subtract(targetLists, finalOutputs);
		
		//hidden layer error is the outputError, split by weights, recombined at hidden nodes.
		double[][] hiddenError = matrix.dot(matrix.transpose(this.who), outputError);
		
		//update the weights for the links between the hidden and output layers
		this.who = matrix.add(this.who, matrix.multiply(this.learningRate, matrix.dot(matrix.multiply(matrix.multiply(outputError, finalOutputs), matrix.subtract(1.0, finalOutputs)), matrix.transpose(hiddenOutputs))));
		
		//update the weights for the links between the input and hidden layers
		this.wih = matrix.add(this.wih, matrix.multiply(this.learningRate, matrix.dot(matrix.multiply(matrix.multiply(hiddenError, hiddenOutputs), matrix.subtract(1.0, hiddenOutputs)), matrix.transpose(inputLists))));
	}
	
	public double[][] query(double[][] inputLists){
		inputLists = matrix.transpose(inputLists);
		
		//calculate signals into hidden layer.
		double[][] hiddenInputs = matrix.dot(this.wih, inputLists);
		double[][] hiddenOutputs = matrix.sigmoidMatrix(hiddenInputs);
		
		//calculate signals into output layer.
		double[][] finalInputs = matrix.dot(this.who, hiddenOutputs);
		double[][] finalOutputs = matrix.sigmoidMatrix(finalInputs);
		
		return finalOutputs;
	}
}
