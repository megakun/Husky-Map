package huskymaps.searching;

import autocomplete.Autocomplete;
import autocomplete.DefaultTerm;
import autocomplete.Term;
import huskymaps.graph.Node;
import huskymaps.graph.StreetMapGraph;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @see Searcher
 */
public class DefaultSearcher extends Searcher {
    private final Autocomplete searcherUsed;
    private final HashMap<String, List<Node>> allLocations;

    public DefaultSearcher(StreetMapGraph graph) {
        allLocations = new HashMap<>();
        List<Term> termList = new LinkedList<>();

        for (Node vertex : graph.allNodes()) {
            if (vertex.name() != null) {
                String name = vertex.name();
                int weight = vertex.importance();

                Term newTerm = createTerm(name, weight);
                termList.add(newTerm);
                if (!allLocations.containsKey(name)) {
                    List<Node> nodes = new ArrayList<>();
                    allLocations.put(name, nodes);
                }
                allLocations.get(name).add(vertex);

            }
        }
        Term[] termsArray = new DefaultTerm[termList.size()];
        for (int i = 0; i < termList.size(); i++) {
            termsArray[i] = termList.get(i);
        }

        searcherUsed = createAutocomplete(termsArray);


    }

    @Override
    protected Term createTerm(String name, int weight) {
        return new DefaultTerm(name, weight);
    }

    @Override
    protected Autocomplete createAutocomplete(Term[] termsArray) {
        return new Autocomplete(termsArray);
    }

    @Override
    public List<String> getLocationsByPrefix(String prefix) {
        Term[] result = searcherUsed.findMatchesForPrefix(prefix);
        HashSet<String> matchSet = new HashSet<>();
        List<String> matches = new ArrayList<>();

        for (int i = result.length - 1; i >= 0; i--) {
            if (result[i].query() != null) {
                matchSet.add(result[i].query());
            }
        }

        matches.addAll(matchSet);

        return matches;
    }

    @Override
    public List<Node> getLocations(String locationName) {
        List<Node> result = allLocations.get(locationName);
        return result;
    }
}
