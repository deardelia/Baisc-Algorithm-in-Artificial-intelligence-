import java.util.List;
import java.util.ArrayList;

/**
 * Fill in the implementation details of the class DecisionTree using this file. Any methods or
 * secondary classes that you want are fine but we will only interact with those methods in the
 * DecisionTree framework.
 */
public class DecisionTreeImpl {
	public DecTreeNode root;
	public List<List<Integer>> trainData;
	public int maxPerLeaf;
	public int maxDepth;
	public int numAttr;

	// Build a decision tree given a training set
	DecisionTreeImpl(List<List<Integer>> trainDataSet, int mPerLeaf, int mDepth) {
		this.trainData = trainDataSet;
		this.maxPerLeaf = mPerLeaf;
		this.maxDepth = mDepth;
		if (this.trainData.size() > 0) this.numAttr = trainDataSet.get(0).size() - 1;
		this.root = buildTree(this.trainData, 0);
	}
	
	private DecTreeNode buildTree(List<List<Integer>> dataset, int curDepth ) {
		// TODO: add code here
		DecTreeNode node = null;

		/**
		 * judge whether should return
		 * (1) current instances all in one classes
		 * (2) reach the max depth
		 * (3) the instances for this node <= mperleaf
		 * (4) maximum IG = 0
 		 */
		 int oneClassCount = 0;
		 int zeroClassCount = 0;
		 for (List<Integer> list : dataset) {
		 	if (list.get(list.size()-1) == 0) {
				zeroClassCount += 1;
			} else {
		 		oneClassCount += 1;
			}
		 }


		 if ((zeroClassCount + oneClassCount) <= this.maxPerLeaf) {
		 	if (zeroClassCount > oneClassCount) {
				node = new DecTreeNode(0,-1,-1);
			} else {
		 		node = new DecTreeNode(1,-1,-1);
			 }
		 } else if (curDepth >= this.maxDepth){
			 if (zeroClassCount > oneClassCount) {
				 node = new DecTreeNode(0,-1,-1);
			 } else {
				 node = new DecTreeNode(1,-1,-1);
			 }
		 } else if (zeroClassCount == 0) {
		 	 node = new DecTreeNode(1,-1,-1);
		 } else if (oneClassCount == 0) {
		 	 node = new DecTreeNode(0,-1,-1);
		 } else {
			 // judge whether the max IG == 0
			 // if not, it is not leaf node;
			 // get best attribute and its threshold
			 attributePair bestAttributeGroup = bestAttributeAthreshold(dataset);
			 if (bestAttributeGroup == null) {
				 if (zeroClassCount > oneClassCount) {
					 node = new DecTreeNode(0,-1,-1);
				 } else {
					 node = new DecTreeNode(1,-1,-1);
				 }
			 } else {
				 int attribute = bestAttributeGroup.attribute;
				 int threshold = bestAttributeGroup.threshold;

				 List<List<Integer>> s0 = new ArrayList<>();
				 List<List<Integer>> s1 = new ArrayList<>();
				 for(List<Integer> dataPoint: dataset) {
					 if(dataPoint.get(attribute) <= threshold) {
						 s0.add(dataPoint);
					 } else if(dataPoint.get(attribute) > threshold) {
						 s1.add(dataPoint);
					 }

				 }
				 if(s0.size() == dataset.size()) {
					 //System.out.println("dds");
				 }
				 if(s1.size() == dataset.size()) {
					 //System.out.println("ddss");
				 }
				 DecTreeNode nodeForZero = buildTree(s0, curDepth+1);
				 DecTreeNode nodeForOne = buildTree(s1, curDepth+1);

				 node = new DecTreeNode(-1, attribute, threshold);
				 node.left = nodeForZero;
				 node.right =  nodeForOne;
			 }
		 }

		return node;
	}
	
	public int classify(List<Integer> instance) {
		// TODO: add code here
		// Note that the last element of the array is the label.
		int classFromTree = performTreeTraversal(this.root, instance);
		return classFromTree;
	}

	private int performTreeTraversal(DecTreeNode node, List<Integer> dataPoint) {
		int classFromTree = -1;
		if(node.isLeaf()) {
			return node.classLabel;
		} else {
			if(dataPoint.get(node.attribute) <= node.threshold) {
				return performTreeTraversal(node.left, dataPoint);
			}  else if(dataPoint.get(node.attribute) > node.threshold) {
				return performTreeTraversal(node.right, dataPoint);
			}
		}
		return classFromTree;
	}

	private attributePair bestAttributeAthreshold(List<List<Integer>> dataset) {
		attributePair bestPair = null;

		double initialEn = initialEtropy(dataset);
		double maxInformationGain = -1;
		int attribute = 0;

		for(attribute = 0; attribute < this.numAttr; attribute++) {
			for (int threshold = 1; threshold <= 10; threshold++) {
				attributePair pair = new attributePair(attribute, threshold);
				double informationGain = 0;
				informationGain = informationGainForAttribute(dataset, initialEn, pair);

				if(informationGain > maxInformationGain) {
					maxInformationGain = informationGain;
					bestPair = pair;
				}
			}
		}
		if (maxInformationGain == 0) {
			return null;
		}
		return bestPair;
	}

	private double informationGainForAttribute(List<List<Integer>> dataset, double initialEn,
											   attributePair attributePair) {
		int zeroZero = 0;
		int zeroOne = 0;
		int oneZero = 0;
		int oneOne = 0;
		int attribute = attributePair.attribute;
		int threshold = attributePair.threshold;

		for(List<Integer> dataPoint: dataset) {
			if(dataPoint.get(attribute) <= threshold) {
				if(dataPoint.get(dataPoint.size()-1) == 0) {
					zeroZero++;
				}
				else if(dataPoint.get(dataPoint.size()-1) == 1) {
					zeroOne++;
				}
			} else if(dataPoint.get(attribute) > threshold) {
				if(dataPoint.get(dataPoint.size()-1) == 0) {
					oneZero++;
				}
				else if(dataPoint.get(dataPoint.size()-1) == 1) {
					oneOne++;
				}
			}
		}

		double entropyLeft = calculateEntropy(zeroZero, zeroOne);
		double entropyRight = calculateEntropy(oneZero, oneOne);
		double inforGain = informationGain(initialEn, zeroZero, zeroOne, oneZero, oneOne, entropyLeft,
				entropyRight);
		return inforGain;

	}

	private double informationGain(double initialEntropy, int zeroZero, int zeroOne, int oneZero, int oneOne,
										  double entropyLeft, double entropyRight) {
		int total = zeroZero + zeroOne + oneZero + oneOne;
		double finalEntropy = ((double)((double)zeroZero + (double)zeroOne)/((double)total))*(entropyLeft) + ((double)((double)oneZero + (double)oneOne)/((double)total))*(entropyRight);
		double informationGain = initialEntropy - finalEntropy;
		return informationGain;
	}

	private double initialEtropy (List<List<Integer>> dataset) {
		double initialEn = 0;
		int zeroClassCount = 0;
		int oneClassCount = 0;

		for(List<Integer> list: dataset) {
			if(list.get(list.size()-1) == 0) {
				zeroClassCount += 1;
			} else if(list.get(list.size()-1) == 1) {
				oneClassCount += 1;
			}
		}

		initialEn = calculateEntropy(zeroClassCount, oneClassCount);
		return initialEn;
	}

	private double calculateEntropy(int zeroCount, int oneCount) {
		double entropy = 0;
		double zeroFraction = 0;
		double oneFraction = 0;
		double zeroFractionLog = 0;
		double oneFractionLog = 0;

		if(zeroCount != 0) {
			zeroFraction = (double)((double)(zeroCount))/((double)(zeroCount + oneCount));
			zeroFractionLog = Math.log(zeroFraction)/Math.log((double)2);
		}

		if(oneCount != 0) {
			oneFraction = (double)((double)(oneCount))/((double)(zeroCount + oneCount));
			oneFractionLog = Math.log(oneFraction)/Math.log((double)2);
		}

		entropy = -(zeroFraction*zeroFractionLog + oneFraction*oneFractionLog);
		return entropy;
	}
	
	// Print the decision tree in the specified format
	public void printTree() {
		printTreeNode("", this.root);
	}

	public void printTreeNode(String prefixStr, DecTreeNode node) {
		String printStr = prefixStr + "X_" + node.attribute;
		System.out.print(printStr + " <= " + String.format("%d", node.threshold));
		if(node.left.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.left.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.left);
		}
		System.out.print(printStr + " > " + String.format("%d", node.threshold));
		if(node.right.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.right.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.right);
		}
	}
	
	public double printTest(List<List<Integer>> testDataSet) {
		int numEqual = 0;
		int numTotal = 0;
		for (int i = 0; i < testDataSet.size(); i ++)
		{
			int prediction = classify(testDataSet.get(i));
			int groundTruth = testDataSet.get(i).get(testDataSet.get(i).size() - 1);
			System.out.println(prediction);
			if (groundTruth == prediction) {
				numEqual++;
			}
			numTotal++;
		}
		double accuracy = numEqual*100.0 / (double)numTotal;
		System.out.println(String.format("%.2f", accuracy) + "%");
		return accuracy;
	}
}


class attributePair {

	public int attribute;
	public int threshold;

	public attributePair(int x, int y) {
		this. attribute = x;
		this.threshold = y;
	}

}
