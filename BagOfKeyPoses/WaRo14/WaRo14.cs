using System.Collections.Generic;
using BagOfKeyPoses;
using TrainDataType = Util.AssociativeArray<string, System.Collections.Generic.List<System.Collections.Generic.List<double[]>>>;
using Util;
using System.Linq;
using System.Text;
using System.IO;
using System;
using System.Runtime.Serialization.Formatters.Binary;
using System.ComponentModel;

namespace SampleUsage
{
    class WaRo14
    {
        public const string dataset_path = "../../../../RecogidaDatos";
        static void Main(string[] args)
        {
          //testSequenceRecognition ();
          //testContinuousRecognition();
          CalculateAccuracy();

        }
        private static void CalculateAccuracy()
        {
           for (int action = 1; action <= 14; action++)
            {
                int right=0;
                int total;
				string result_action_path = Path.Combine(string.Concat("../../../../results/Action", action, ".txt"));
				string accuracy_path = Path.Combine(string.Concat("../../../../results/Accuray.txt"));
                char[] separator = new char[] { ' ', '\n', '\r' };

                if (!File.Exists(result_action_path))
                    continue;
                
                StreamReader fd = new StreamReader(result_action_path);
                string actionsInLine = fd.ReadToEnd();
                fd.Close();
                string[] separatedActionsInLine = actionsInLine.Split(separator, StringSplitOptions.RemoveEmptyEntries);
               
                List<uint[]> parsedActionResult = new List<uint[]>();
                uint[] line = new uint[5];
                int i = 0;
                for (int iter = 0; iter < separatedActionsInLine.Length; iter++)
                {
                    line[i++] = Convert.ToUInt32(separatedActionsInLine[iter]);

                    if (i == 5)
                    {
                        i = 0;
                        parsedActionResult.Add((uint[])line.Clone());
                    }
                }
                int numberOfSequences = parsedActionResult.Count;
                total = numberOfSequences;
                for (int iter = 0; iter < numberOfSequences; iter++)
                {
                    uint[] possibleActions = parsedActionResult[iter];
                    for (int j = 0; j < 5; j++)
                    {
						if (action == possibleActions[j])
                        {
                            right++;
                            break;
                        }
                        if (possibleActions[j] == 0)
                        {
                            total--;
                            break;
                        }
                    }
                }
                /*List<uint> parsedActionResult = new List<uint>();
                
                 for (int iter = 0; iter < separatedActionsInLine.Length; iter++)
                 {
                     parsedActionResult.Add(Convert.ToUInt32(separatedActionsInLine[iter]));
                    
                 }
                 int numberOfSequences = parsedActionResult.Count;
                 total = numberOfSequences;
                 for (int iter = 0; iter < numberOfSequences; iter++)
                 {
                     if (action == parsedActionResult[iter])
                         right++;                        
                     if ( parsedActionResult[iter] == 0)
                        total--;
                 }
                
               */
                double accuracy = ((double)right / (double)total) * 100;
                using (System.IO.StreamWriter file = new System.IO.StreamWriter(accuracy_path, true))
                {
                    file.WriteLine("Accuracy action "+action +" " + accuracy+ "%");
                }
                Console.WriteLine("Accuracy action " + action + " " + accuracy);

            }
            
            for (int actor = 1; actor <= 15; actor++)
            {
                int right = 0;
                int total;
				string accuracy_path = Path.Combine(string.Concat("../../../../results/Accuray.txt"));
				string result_action_path = Path.Combine(string.Concat("../../../../results/Actor", actor, ".txt" ));
				string groundtruth_action_path = Path.Combine(string.Concat("../../../../truth/actor", actor, "Esqueleto_frame_segmented.txt"));
                char[] separator = new char[] { ' ', '\n', '\r' };

                if (!File.Exists(result_action_path))
                    continue;

                StreamReader fd = new StreamReader(result_action_path);
                string actionsInLine = fd.ReadToEnd();
                fd.Close();
                string[] separatedActionsInLine = actionsInLine.Split(separator, StringSplitOptions.RemoveEmptyEntries);

                if (File.Exists(groundtruth_action_path))
                {
                    Console.WriteLine("Accuracy" );
                }
                StreamReader gd_fd = new StreamReader(groundtruth_action_path);
                string groundTruthActions = gd_fd.ReadToEnd();
                fd.Close();
                string[] groundTruthActionsAfterSplitting = groundTruthActions.Split(separator, StringSplitOptions.RemoveEmptyEntries);

                // Obtaining the third column where the groundTruth action is
                List<uint> groundTruth= new List<uint>();
                
                for (int iter = 0; iter < groundTruthActionsAfterSplitting.Length; iter=iter+3)
                    groundTruth.Add(Convert.ToUInt32(separatedActionsInLine[iter]));


                List<uint[]> parsedActionResult = new List<uint[]>();
                uint[] line = new uint[5];
                int i = 0;
                for (int iter = 0; iter < separatedActionsInLine.Length; iter++)
                {
                    line[i++] = Convert.ToUInt32(separatedActionsInLine[iter]);

                    if (i == 5)
                    {
                        i = 0;
                        parsedActionResult.Add((uint[])line.Clone());
                        
                    }
                }
                int numberOfSequences = parsedActionResult.Count;
                total = numberOfSequences;
                for (int iter = 0; iter < numberOfSequences; iter++)
                {
                    uint[] possibleActions = parsedActionResult[iter];
                    for (int j = 0; j < 5; j++)
                    {
                        if (groundTruth[iter] == possibleActions[j])
                        {
                            right++;
                            break;
                        }
                        if (possibleActions[j] == 0)
                        {
                            total--;
                            break;
                        }
                    }
                }
                
                double accuracy = ((double)right / (double)total) * 100;
                using (System.IO.StreamWriter file = new System.IO.StreamWriter(accuracy_path, true))
                {
                    file.WriteLine("Accuracy actor " + actor + " " + accuracy + "%");
                }
                Console.WriteLine("Accuracy  actor " + actor + " " + accuracy);

            }
        }


        
        private static void testSequenceRecognition()
        {
         
            // prepare learnings params
            LearningParams learning_params = new LearningParams();
            List<string> class_labels = new List<string>();
            class_labels.Add("1");
            class_labels.Add("2");
            class_labels.Add("3");
            class_labels.Add("4");
            class_labels.Add("5");
            class_labels.Add("6");
            class_labels.Add("7");
            class_labels.Add("8");
            class_labels.Add("9");
            class_labels.Add("10");
            class_labels.Add("11");
            class_labels.Add("12");
            class_labels.Add("13");
            class_labels.Add("14");

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
            for (int i = 1; i <= 14; i++)
            {
                generateTrainData(train_data, i);
            }

            // Train.
            BoKP bokp = new BoKP(learning_params);
            bokp.Train(train_data.Dictionary);
            Console.WriteLine("SystemTrained!");

            string typeOfTesting = "sequence";


            for (int action = 1; action < 15; action++)
            {
                Console.WriteLine("Action " + action);
                List<List<double[]>> test_data = generateTestingData(typeOfTesting, action);
                int test_data_size = test_data.Count;
                if (test_data_size == 0)
                    continue;
                // Test.
                for (int sequence = 0; sequence < test_data_size; sequence++)
                {
					string pathToResultFile = Path.Combine(string.Concat("../../../../results/Action", action, ".txt"));

                    if (test_data[sequence].Count == 0)
                    {
                        using (System.IO.StreamWriter file = new System.IO.StreamWriter(pathToResultFile, true))
                        {
                            file.WriteLine("0");
                        }
                        Console.WriteLine("Action " + action + " Sequence " + sequence + ": Empty ");
                        continue;
                    }
                    // Returning the 5 more plausible actions 
                    string[] recognition = bokp.EvaluateSequenceRet5(test_data[sequence]);


                    using (System.IO.StreamWriter file = new System.IO.StreamWriter(pathToResultFile, true))
                    {
                        file.WriteLine(recognition[0] + " " + recognition[1] + " " + recognition[2] + " " + recognition[3] + " " + recognition[4]);
                    }
                    Console.WriteLine("Action " + action + " Sequence " + sequence + ": " + recognition[0] + " " + recognition[1] + " " + recognition[2] + " " + recognition[3] + " " + recognition[4]);

                    // Returning just the first more plausible action
                   /* string recognition = bokp.EvaluateSequence(test_data[sequence]);
                    using (System.IO.StreamWriter file = new System.IO.StreamWriter(pathToResultFile, true))
                    {
                        file.WriteLine(recognition+"\n");
                    }
                    Console.WriteLine("Action " + action + " Sequence " + sequence + ": " + recognition);*/
                }
            }
        }



        private static void testContinuousRecognition()
        {
            LearningParams learning_params = new LearningParams();
            List<string> class_labels = new List<string>();
            class_labels.Add("1");
            class_labels.Add("2");
            class_labels.Add("3");
            class_labels.Add("4");
            class_labels.Add("5");
            class_labels.Add("6");
            class_labels.Add("7");
            class_labels.Add("8");
            class_labels.Add("9");
            class_labels.Add("10");
            class_labels.Add("11");
            class_labels.Add("12");
            class_labels.Add("13");
            class_labels.Add("14");

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
            for (int i = 1; i <= 14; i++)
            {
                generateTrainData(train_data, i);
            }

            // Train.
            BoKP bokp = new BoKP(learning_params);
            bokp.Train(train_data.Dictionary);
            Console.WriteLine("SystemTrained!");

            // Prepare test sequence
            string typeOfTesting = "continuous";

            // Test.
            for (int action = 1; action <= 15; action++)
            {
                Console.WriteLine("Action " + action);
                List<List<double[]>> test_data = generateTestingData(typeOfTesting, action);
                int test_data_size = test_data.Count;
                if (test_data_size == 0)
                    break;
                // Test.
                for (int sequence = 0; sequence < test_data_size; sequence++)
                {
                    string pathToResultFile = Path.Combine(string.Concat("../../../../results/Actor", action, ".txt"));

                    if (test_data[sequence].Count == 0)
                    {
                        using (System.IO.StreamWriter file = new System.IO.StreamWriter(pathToResultFile, true))
                        {
                            file.WriteLine("0 0 0 0 0");
                        }
                        Console.WriteLine("Action " + action + " Sequence " + sequence + ": Empty ");
                        continue;
                    }
                    string[] recognition = bokp.EvaluateSequenceRet5(test_data[sequence]);

                    using (System.IO.StreamWriter file = new System.IO.StreamWriter(pathToResultFile, true))
                    {
                        file.WriteLine(recognition[0] + " " + recognition[1] + " " + recognition[2] + " " + recognition[3] + " " + recognition[4]);
                    }
                    Console.WriteLine("Action " + action + " Sequence " + sequence + ": " + recognition[0] + " " + recognition[1] + " " + recognition[2] + " " + recognition[3] + " " + recognition[4]);
                }
            }
        }
        private static void generateTrainData(TrainDataType train_data, int action)
        {
            string path = Path.Combine(dataset_path, "actionsFiles/training");
            string action_path = Path.Combine(path, string.Concat(string.Concat("action", action.ToString()), "s"));
            string actionName = getActionName(action);

            if (Directory.Exists(path))
            {
                List<string> fileEntries = Directory.GetFiles(path).ToList();

                List<string> actionEntries = new List<string>();
                foreach (string fileName in fileEntries)
                {
                    if (fileName.Contains(action_path))
                        actionEntries.Add(fileName);
                }

                foreach (string actionFileName in SortByLength(actionEntries))
                {
                    List<double[]> sequenceList = new List<double[]>();

                    string[] sequence = obtainJointsFromFile(actionFileName);
                    for (int iter = 0; iter < sequence.Length; iter++)
                        sequence[iter] = sequence[iter].Replace(".", ",");
                    sequenceList = generateListOfFrames(sequence);
                    train_data[actionName].Add(sequenceList);
                }
            }
            else
            {
                Console.WriteLine("Error! 'path' is not a valid directory");
            }
        }

        private static List<List<double[]>> generateTestingData(string typeOfTesting, int action)
        {
            List<List<double[]>> testList = new List<List<double[]>>();

            if (typeOfTesting.Equals("sequence") || typeOfTesting.Equals("continuous"))
            {
                string path = Path.Combine(dataset_path, string.Concat("actionsFiles/testing-", typeOfTesting));
                string action_path = Path.Combine(path, string.Concat(string.Concat("action", action.ToString()), "s"));

                if (Directory.Exists(path))
                {
                    List<string> fileEntries = Directory.GetFiles(path).ToList();

                    List<string> actionEntries = new List<string>();
                    foreach (string fileName in fileEntries)
                    {
                        if (fileName.Contains(action_path))
                            actionEntries.Add(fileName);
                    }

                    foreach (string actionFileName in SortByLength(actionEntries))
                    {
                        List<double[]> sequenceList = new List<double[]>();

                        string[] sequence = obtainJointsFromFile(actionFileName);
                        for (int iter = 0; iter < sequence.Length; iter++)
                            sequence[iter] = sequence[iter].Replace(".", ",");
                        sequenceList = generateListOfFrames(sequence);
                        testList.Add(sequenceList);
                    }
                }
                else
                {
                    Console.WriteLine("Error! 'path' is not a valid directory");
                }
            }
            else
            {
                Console.WriteLine("Error! 'typeOfTesting' is not a valid type");
            }

            return testList;
        }

        private static string[] obtainJointsFromFile(string fileName)
        {
            char[] separator = new char[] { ' ' };

            StreamReader fd = new StreamReader(fileName);
            string sequence = fd.ReadToEnd();
            fd.Close();

            return sequence.Split(separator, StringSplitOptions.RemoveEmptyEntries);
        }

        private static List<double[]> generateListOfFrames(string[] sequence)
        {
            int n = sequence.Count();
            List<double[]> sequenceList = new List<double[]>();

            for (int i = 0; i < n; i += 45)
            {
                double[] frame = new double[45];
                for (int j = 0; j < 45; j++)
                {
                    try
                    {
                        frame[j] = Convert.ToDouble(sequence[i + j]);
                    }
                    catch (FormatException)
                    {
                        Console.WriteLine("Unable to convert to a Double: '{0}'", sequence[i + j]);
                    }

                }
                sequenceList.Add(frame);
            }

            return sequenceList;
        }

        private static string getActionName(int action)
        {
            string res = "";
            switch (action)
            {
                case 0:
                    res = "0";
                    break;
                case 1:
                    res = "1";
                    break;
                case 2:
                    res = "2";
                    break;
                case 3:
                    res = "3";
                    break;
                case 4:
                    res = "4";
                    break;
                case 5:
                    res = "5";
                    break;
                case 6:
                    res = "6";
                    break;
                case 7:
                    res = "7";
                    break;
                case 8:
                    res = "8";
                    break;
                case 9:
                    res = "9";
                    break;
                case 10:
                    res = "10";
                    break;
                case 11:
                    res = "11";
                    break;
                case 12:
                    res = "12";
                    break;
                case 13:
                    res = "13";
                    break;
                case 14:
                    res = "14";
                    break;
                default:
                    Environment.Exit(1);
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
    }
}