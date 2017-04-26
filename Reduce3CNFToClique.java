import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class reduces the given formula in 3-Conjunctive Normal Form into a GraphViz representation of a
 * graph such as could be visualized for an instance of CLIQUE. (Note this program does not solve for CLIQUE!)
 *
 * To use, enter desired formula as the String SAT below, update values for and/or/not if needed, and run
 * program. The text output to the console can be copied and read by your favorite GraphViz viewing tool.
 * @author kirstenjensen42
 *
 */
public class Reduce3CNFToClique {


	static final String AND = "∧";
	static final String OR = "∨";
	static final String NOT = "¬";
	static final String SAT = "(X ∨ Y ∨ Z) ∧ (X ∨ ¬Y ∨ ¬Z) ∧ (¬X ∨ Y ∨ ¬Z) ∧ (¬X ∨ ¬Y ∨ ¬Z)";


	public static void main(String[] args) {

		ArrayList<LinkedList<Literal>> data = new ArrayList<LinkedList<Literal>>();


		// parse clauses at AND
		String[] satClauses = SAT.split(AND);
		String[][] clauseVariables = new String[satClauses.length][3];

		// remove parenthesis and such, parse literals at OR
		for (int i = 0; i < satClauses.length; i++) {
			satClauses[i] = satClauses[i].trim();
			satClauses[i] = satClauses[i].replace("(", "");
			satClauses[i] = satClauses[i].replace(")", "");
			clauseVariables[i] = satClauses[i].split(OR);
		}

		// Create each clause as a linked list of Literal objects
		for (int i = 0; i < clauseVariables.length; i++){

			LinkedList<Literal> clause = new LinkedList<Literal>();
			Reduce3CNFToClique m = new Reduce3CNFToClique();

			for (int j = 0; j < 3; j++){
				clauseVariables[i][j] = clauseVariables[i][j].trim();
				if (clauseVariables[i][j].contains(NOT)){
					clauseVariables[i][j] = clauseVariables[i][j].replace(NOT, "");
					Reduce3CNFToClique.Literal var = m.new Literal(i,clauseVariables[i][j],false);
					clause.add(var);
				} else {
					Reduce3CNFToClique.Literal var = m.new Literal(i, clauseVariables[i][j], true);
					clause.add(var);
				}
			}

			// add clause to ArrayList of clauses
			data.add(clause);
		}

		// print out Literals as vertices of a graph with an edge to all non conflicting vertex pairs
		// where no vertices of any pair are from the same clause grouping
		System.out.println("graph G{");
		System.out.println("    label=\"" + SAT + "\";");
		for (int i = 0; i < data.size() - 1; i++){
			System.out.println("    subgraph cluster_" + i + " {");
			for (int j = i + 1; j < data.size(); j++) {
				for (Literal item : data.get(i)){
					for (Literal compare : data.get(j)) {
						if (!item.var().equals(compare.var()) || item.bool() == compare.bool()) {
							System.out.println("        " + item.toString() + "--" + compare.toString() + ";");
						}
					}
				}
			}
			System.out.println("    }");
		}
		System.out.println("}");

	}


	/**
	 * The Literal object used above.
	 * @author kirstenjensen42
	 *
	 */
	private class Literal {

		int group;
		String var;
		boolean bool;

		public Literal(int group, String var, boolean bool){
			this.group = group;
			this.var = var;
			this.bool = bool;
		}

		public String var(){
			return var;
		}

		public boolean bool(){
			return bool;
		}

		public String toString(){
			if (!bool) {
				return "\"" + group + ":" + NOT + var + "\"";
			} else {
				return "\"" + group + ":" + var + "\"";
			}
		}

	}
}

