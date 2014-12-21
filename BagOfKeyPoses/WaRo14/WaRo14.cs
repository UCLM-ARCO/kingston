﻿using System.Collections.Generic;
using BagOfKeyPoses;
using TrainDataType = Util.AssociativeArray<string, System.Collections.Generic.List<System.Collections.Generic.List<double[]>>>;
using Util;
using System.Linq;
using System.Text;
using System.IO;
using System;

namespace WaRo14 
{
	class WaRo14
	{
		public const string dataset_path = "/home/roberto/Documents/projects/kingston/RecogidaDatos";

		static void Main(string[] args)
		{
			//testSequenceRecognition();
			testContinuousRecognition ();

		}

		private static void testSequenceRecognition()
		{
			// prepare learnings params
			LearningParams learning_params = new LearningParams ();
			List<string> class_labels = new List<string> ();
			class_labels.Add ("mirar_reloj");
			class_labels.Add ("cruzar_brazos");
			class_labels.Add ("rascarse_cabeza");
			/*class_labels.Add ("sentarse");
			class_labels.Add ("levantarse");
			class_labels.Add ("darse_vuelta");
			class_labels.Add ("caminar");
			class_labels.Add ("saludar");
			class_labels.Add ("dar_punetazo");
			class_labels.Add ("dar_patada");
			class_labels.Add ("senalar");
			class_labels.Add ("coger");
			class_labels.Add ("lanzar_sobre_cabeza");
			class_labels.Add ("lanzar_abajo_arriba");*/

			learning_params.ClassLabels = class_labels;
			learning_params.Clustering = LearningParams.ClusteringType.Kmeans;
			learning_params.InitialK = 32;
			learning_params.FeatureSize = 45;
			/*learning_params.SetDistThresholdsSelection(400);
			learning_params.SetEvidThresholdsSelection(300);
			learning_params.UseZones = true;*/

			// Sliding window
			//learning_params.MinFrames = 5;
			//learning_params.MaxFrames = 30;

			// prepare train data
			TrainDataType train_data = new TrainDataType();
			for (int i = 1; i <= 3; i ++) {
				generateTrainData (train_data, i);
			}

			// Train.
			BoKP bokp = new BoKP(learning_params);
			bokp.Train(train_data.Dictionary);
			Console.WriteLine ("SystemTrained!");

			// Prepare test sequence
			int action = 1;
			int sequence = 0;
			string typeOfTesting = "sequence";
			List<List<double[]>> test_data = generateTestingData (typeOfTesting, action);
			
			// Test.
			string recognition = bokp.EvaluateSequence(test_data[sequence]);
			
			// Print result.
			Console.WriteLine("The test sequence has been recognized as " + recognition);
		}

		private static void testContinuousRecognition()
		{
			// prepare learnings params
			LearningParams learning_params = new LearningParams ();
			List<string> class_labels = new List<string> ();
			class_labels.Add ("mirar_reloj");
			class_labels.Add ("cruzar_brazos");
			class_labels.Add ("rascarse_cabeza");
			/*class_labels.Add ("sentarse");
			class_labels.Add ("levantarse");
			class_labels.Add ("darse_vuelta");
			class_labels.Add ("caminar");
			class_labels.Add ("saludar");
			class_labels.Add ("dar_punetazo");
			class_labels.Add ("dar_patada");
			class_labels.Add ("senalar");
			class_labels.Add ("coger");
			class_labels.Add ("lanzar_sobre_cabeza");
			class_labels.Add ("lanzar_abajo_arriba");*/

			learning_params.ClassLabels = class_labels;
			learning_params.Clustering = LearningParams.ClusteringType.Kmeans;
			learning_params.InitialK = 32;
			learning_params.FeatureSize = 45;
			learning_params.SetDistThresholdsSelection(400);
			learning_params.SetEvidThresholdsSelection(300);
			learning_params.UseZones = true;

			// Sliding window
			learning_params.MinFrames = 5;
			learning_params.MaxFrames = 30;

			// prepare train data
			TrainDataType train_data = new TrainDataType();
			for (int i = 1; i <= 3; i ++) {
				generateTrainData (train_data, i);
			}

			// Train.
			BoKP bokp = new BoKP(learning_params);
			bokp.Train(train_data.Dictionary);
			Console.WriteLine ("SystemTrained!");

			// Prepare test sequence
			int action = 1;
			int sequence = 0;
			string typeOfTesting = "continuous";
			List<List<double[]>> test_data = generateTestingData (typeOfTesting, action);

			// Test.
			List<string> recognition = bokp.EvaluateCHARSequence(test_data[sequence]);

			// Print result.
			Console.WriteLine("The test sequence has been recognized as: ");
			foreach (string frame in recognition) Console.WriteLine(frame);
		}

		private static void generateTrainData(TrainDataType train_data, int action)
		{
			string path = Path.Combine(dataset_path, "actionsFiles/training");
			string action_path = Path.Combine (path, string.Concat(string.Concat ("action", action.ToString()), "s"));
			string actionName = getActionName (action);

			if (Directory.Exists (path)) {
				List<string> fileEntries = Directory.GetFiles (path).ToList ();

				List<string> actionEntries = new List<string> ();
				foreach (string fileName in fileEntries) {
					if (fileName.Contains (action_path))
						actionEntries.Add (fileName);
				}

				foreach (string actionFileName in SortByLength(actionEntries)) {
					List<double[]> sequenceList = new List<double[]> ();

					string[] sequence = obtainJointsFromFile (actionFileName);
					sequenceList = generateListOfFrames (sequence);
					train_data [actionName].Add (sequenceList);
				}
			} else {
				Console.WriteLine ("Error! 'path' is not a valid directory");
			}
		}

		private static List<List<double[]>> generateTestingData(string typeOfTesting, int action)
		{
			List<List<double[]>> testList = new List<List<double[]>> ();

			if (typeOfTesting.Equals ("sequence") || typeOfTesting.Equals ("continuous")) {
				string path = Path.Combine (dataset_path, string.Concat ("actionsFiles/testing-", typeOfTesting));
				string action_path = Path.Combine (path, string.Concat (string.Concat ("action", action.ToString ()), "s"));

				if (Directory.Exists (path)) {
					List<string> fileEntries = Directory.GetFiles (path).ToList ();

					List<string> actionEntries = new List<string> ();
					foreach (string fileName in fileEntries) {
						if (fileName.Contains (action_path))
							actionEntries.Add (fileName);
					}

					foreach (string actionFileName in SortByLength(actionEntries)) {
						List<double[]> sequenceList = new List<double[]> ();

						string[] sequence = obtainJointsFromFile (actionFileName);
						sequenceList = generateListOfFrames (sequence);
						testList.Add (sequenceList);
					}
				} else {
					Console.WriteLine ("Error! 'path' is not a valid directory");
				}
			} else {
				Console.WriteLine ("Error! 'typeOfTesting' is not a valid type");
			}

			return testList;
		}

		private static string[] obtainJointsFromFile (string fileName)
		{
			char[] separator = new char[] { ' ' };

			StreamReader fd = new StreamReader (fileName);
			string sequence = fd.ReadToEnd ();
			fd.Close ();

			return sequence.Split (separator, StringSplitOptions.RemoveEmptyEntries);
		}

		private static List<double[]> generateListOfFrames(string[] sequence)
		{
			int n = sequence.Count ();
			List<double[]> sequenceList = new List<double[]> ();

			for (int i = 0; i < n; i += 45) {
				double[] frame = new double[45];
				for (int j = 0; j < 45; j++) {
					try {
						frame [j] = Convert.ToDouble (sequence [i+j]);
					} catch (FormatException) {
						Console.WriteLine ("Unable to convert to a Double: '{0}'", sequence [i+j]);
					}
					sequenceList.Add (frame);
				}
			}

			return sequenceList;
		}

		private static string getActionName (int action)
		{
			string res = "";
			switch (action) {
			case 0:
				res = "nada";
				break;
			case 1:
				res = "mirar_reloj";
				break;
			case 2:
				res = "cruzar_brazos";
				break;
			case 3:
				res = "rascarse_cabeza";
				break;
			case 4:
				res = "sentarse";
				break;
			case 5:
				res = "levantarse";
				break;
			case 6:
				res = "darse_vuelta";
				break;
			case 7:
				res = "caminar";
				break;
			case 8:
				res = "saludar";
				break;
			case 9:
				res = "dar_punetazo";
				break;
			case 10:
				res = "dar_patada";
				break;
			case 11:
				res = "senalar";
				break;
			case 12:
				res = "coger";
				break;
			case 13:
				res = "lanzar_sobre_cabeza";
				break;
			case 14:
				res = "lanzar_abajo_arriba";
				break;
			default:
				Environment.Exit (1);
				break;
			}

			return res;
		}

		private static IEnumerable<string> SortByLength(IEnumerable<string> e)
		{
			// Use LINQ to sort the array received and return a copy.
			var sorted = from s in e
				orderby s.Length ascending
				select s;
			return sorted;
		}

		//		foreach (List<double[]> l in train_data[getActionName(i)])
		//			foreach (double[] d in l)
		//				foreach (double dd in d)
		//					Console.WriteLine (dd);
	}
}