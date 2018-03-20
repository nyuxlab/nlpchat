PRES

indexes.Add("PRES", new LiteCalculatorItem("PRES", PRES, null));

public double PRES(Dictionary<string, double> resultTable) { 
	return get_value_by_type(Biber.BBFeature.PRES, 1);
 }

public int CountPresentTense(XMLTree tree)
        {
            int ret = 0;
            if(tree.children!=null&&tree.children.Count>0)
            {
                if(tree.nodeName.Trim().ToUpper().Equals("VP"))
                {
                    XMLTree child = (XMLTree)tree.children[0];
                    switch(child.nodeName.Trim().ToLower())
                    {
                        case "to": ret =-1;break;
                        case "vb":
                        case "vbz": ret = 1;break;
                        default: break;

                    }
                }
                if(tree.children!=null&&tree.children.Count>0)
                    for(int i=0;i<tree.children.Count;i++)
                        ret += this.CountPresentTense((XMLTree)tree.children[i]);
            }
            return ret;
        }


