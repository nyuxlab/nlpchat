FRSLacsm 

public double FRSLacsm(Dictionary<string, double> resultTable) { 
	double[] v = instance.get_sentence_by_index(8, true, 3); 
	return v[0]; 
}
public double[] get_sentence_by_index(int cob_index, bool iscontent, int index)
        {
            int sentenceCount=0;
            int paragraphCount=0;
            
            ArrayList matrix = new ArrayList();

            for (int i = 0; i < this.Text.children.Count; i++)
            {
                paragraphCount++;
                Paragraph prg = (Paragraph)this.Text.children[i];
                for (int j = 0; j < prg.children.Count; j++)
                {
                    sentenceCount++;
                    double[] v = new double[4];

                    float SentenceMin = 10000000;
                    float SentenceMax = 0;
                    float SentenceMean = 0;

                    Sentence sntc = (Sentence)prg.children[j];
                    string[] wtList = sntc.wordTagList.Split(new char[] { '\n' });
                    int wordsInSentence = 0;
                    for (int k = 0; k < wtList.Length; k++)
                    {

                        //Console.WriteLine(wtList[k]);
                        string[] token_tag = wtList[k].Trim().Split("\t".ToCharArray());
                        string tag="";
                        if(token_tag.Length>1){
                            tag = token_tag[1].Trim();
                        }
                        if (!iscontent || identifyContent(tag.ToLower()))
                        {
                            float[] Cobs = (float[])this.wordInfo.Get(wtList[k], "CELEX", "Cobs");
                            if (Cobs != null)
                            {

                                if (SentenceMin > Cobs[cob_index]) SentenceMin = Cobs[cob_index];
                                if (SentenceMax < Cobs[cob_index]) SentenceMax = Cobs[cob_index];
                                SentenceMean += Cobs[cob_index];

                                wordsInSentence++;
                            }
                            
                        }
                    }
                    if (wordsInSentence > 0)
                    {
                        v[0] = (double)sentenceCount;
                        v[1] = SentenceMin;
                        v[2] = SentenceMax;
                        v[3] = SentenceMean * 1.0 / wordsInSentence;
                    }
                    else
                    {
                        v[0] = (double)sentenceCount;
                        v[1] = 0;
                        v[2] = 0;
                        v[3] = 0;
                    }
                   
                    matrix.Add(v);

                }
            }


            double[] u = new double[2];
            double x = 0;
            double x2 = 0;
            int total = 0;
            for(int i=0;i<matrix.Count;i++)
            {
                double[] v = (double[])matrix[i];
                int count = 1;
                try  // in case the value is not valid   cai 6/9/05
                {
                    double t = v[index];//[j][i], Yan 06/07/2005
                    if (t > 0.000001)
                    {
                        double t1 = t * count;
                        x += t1;
                        //x2+=t1*t1;      // This line made the std much larger than the correct
                        x2 += t * t * count; // this is changed on Feb. 6, 2007
                        total += count;
                    }
                }
                catch
                {
                }
                 
            }
            u[0] = 0; u[1] = 0;
            if (total > 0)
                u[0] = x / total;
            if (total > 1)
                u[1] = Math.Sqrt((total * x2 - x * x) / total / (total - 1));

            return u;
        }

