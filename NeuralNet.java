package JNeuralNet;

import java.util.Arrays;

public class NeuralNet {
	
	int[] shape;
	NeuronLayer[] layers;
	
	class NeuronLayer {
		
		Neuron[] neurons;
		
		class Neuron {
	
			double[] weights;
			short tick(short[] inputs) throws Exception {
				short output=0;
				if(weights.length != inputs.length) {
					throw new Exception("Error: Too many or too few inputs to Neuron (Inputs:"+inputs.length+" Weights:"+weights.length+")");
				}
				for(int i=0; i<inputs.length; i++) {
					output += inputs[i]*weights[i];
				}
				return output;
			} // short tick(short[] inputs)
			Neuron(double[] w) {
				weights = w;
			} // Neuron(double[] w)
		
		} // class Neuron
		
		NeuronLayer(double[] w,int numOfNeurons, int numOfWeightsPerNeuron) {
			neurons = new Neuron[numOfNeurons];
			for(int i=0;i<numOfNeurons;i++) {
				neurons[i] = new Neuron(Arrays.copyOfRange(w,numOfWeightsPerNeuron*i,(numOfWeightsPerNeuron*i)+numOfWeightsPerNeuron));
			}
		}
		
		short[] tick(short[] inputs) throws Exception {
			short[] output = new short[neurons.length];
			for(int i=0;i<neurons.length;i++) {
				output[i] = neurons[i].tick(inputs);
			}
			return output;
		}
		
	} // class NeuronLayer
	
	NeuralNet(Genome g) {
		shape = g.getShape();
		layers = new NeuronLayer[shape.length];
		int i=0;
		double[] layerWeights = g.getWeights();
		// 0 and 1 are special cases, because the number of neurons in each layer is dependent on the size of the TWO preceding layers (counting 'inputs' as a layer if necessary)
		layers[0] = new NeuronLayer(Arrays.copyOfRange(layerWeights, 0, shape[0]*RobotGenome.NUM_INPUTS),shape[0],RobotGenome.NUM_INPUTS);
		layers[1] = new NeuronLayer(Arrays.copyOfRange(layerWeights, shape[0]*RobotGenome.NUM_INPUTS, (shape[0]*RobotGenome.NUM_INPUTS) + (shape[0]*shape[1])),shape[1],shape[0]);
		if(shape.length>2) { // sanity check
			for(i=2;i<shape.length;i++) {
				layers[i] = new NeuronLayer(Arrays.copyOfRange(layerWeights, shape[i-2]*shape[i-1], shape[i-2]*shape[i-1] + shape[i]*shape[i-1]),shape[i],shape[i-1]);
			}
		}
	}
	
	short[] tick(short[] inputs) throws Exception {
		short[] output = inputs;
		
		for(int i=0;i<layers.length;i++) {
			output = layers[i].tick(output);
		}
		return output;
	}
	
} // class NeuralNet
