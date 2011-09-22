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
					throw new Exception("Error: Too many or too few inputs to Neuron");
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
		
		NeuronLayer(double[] w,int numOfNeurons) {
			neurons = new Neuron[numOfNeurons];
			for(int i=0;i<numOfNeurons;i++) {
				neurons[i] = new Neuron(Arrays.copyOfRange(w,(w.length/numOfNeurons)*i,(w.length/numOfNeurons)*(i+1)));
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
		for(int i=0;i<shape.length;i++) {
			double[] layerWeights = g.getWeights();
			layers[i] = new NeuronLayer(Arrays.copyOfRange(layerWeights,(layerWeights.length/shape.length)*i,(layerWeights.length/shape.length)*(i+1)),shape[i]);
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
