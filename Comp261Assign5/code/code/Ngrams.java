import java.util.*;

        public class Ngrams {

            private Map<String, List<NgramsNode>> ngramsTable;
            private int ncount;

            public String ngramsProbCal(String text, int n) {

                this.ncount = n;
                ngramsTable = new HashMap<>();

                for (int i = 0; i < text.length() - n; i++) {

                    String prefix = text.substring(i, i + n);
                    char nextChar = text.charAt(i + n);

                    //if the prefix already exist then increment the count
                    if (!ngramsTable.isEmpty() && ngramsTable.get(prefix) != null) {

                        boolean charExisted = false;
                        for (NgramsNode nodePrefix : ngramsTable.get(prefix)) {
                            if (nodePrefix.ngramCharacter == nextChar) {
                                charExisted = true;
                                nodePrefix.ngramsCount += 1.0f; // increment count
                                break;
                            }
                        } // add the new char for this existing prefix
                        if (!charExisted) {
                            NgramsNode nn = new NgramsNode(nextChar, 1.0f);
                            ngramsTable.get(prefix).add(nn);
                        }
                    } else {//add the prefix
                        List<NgramsNode> ngramslist = new ArrayList<>();
                        ngramslist.add(new NgramsNode(nextChar, 1.0f));
                        ngramsTable.put(prefix, new ArrayList<>(ngramslist));
                    }
                }


                // calculate total and then  convert to probability
                for (Map.Entry<String, List<NgramsNode>> entryNode : ngramsTable.entrySet()) {
                    float total = 0.0f;

                    // calculate total first
                    for (NgramsNode nodeKey : ngramsTable.get(entryNode.getKey()))
                        total += nodeKey.ngramsCount;

                    // calculate probability by divide the for each prefix with the total
                    for (NgramsNode nodeKey : ngramsTable.get(entryNode.getKey()))
                        nodeKey.ngramsCount = nodeKey.ngramsCount / total;
                }

                // build string for the ngram table
                StringBuilder completeNgramsTable = new StringBuilder().append("n = " + n + "\n");
                for (Map.Entry<String, List<NgramsNode>> entry : ngramsTable.entrySet())
                    completeNgramsTable.append("Prefix: " + entry.getKey() + entry.getValue().toString() + "\n");


                // return table in string
                return completeNgramsTable.toString();
            }


            /** A helper class that enable us create a character with its count (number of times it appeared
             * in the large text T) when building n-gram table*/
            public static class NgramsNode {
                private char ngramCharacter;
                private float ngramsCount;

                public NgramsNode(char ch, float counts) {
                    this.ngramCharacter = ch;
                    this.ngramsCount = counts;
                }

                @Override
                public String toString() {
                    return "nextCharacter: " + ngramCharacter + "  Probability: " + ngramsCount;
                }
            }

            public String getInformation() {
                StringBuilder sb = new StringBuilder();
                int maxValue = 0;
                sb.append("Some output of Ngram table.\n");
                for (Map.Entry<String, List<NgramsNode>> entry : ngramsTable.entrySet()) {
                    sb.append("Prefix:  " + entry.getKey()  + entry.getValue().toString());
                    maxValue++;
                    if(maxValue > 100) break;
                }
                return sb.toString();
            }

        }
