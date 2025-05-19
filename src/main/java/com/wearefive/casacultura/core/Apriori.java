/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.wearefive.casacultura.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 *
 * @author juanje
 */
public class Apriori {
    public static List<Set<Integer>> getFrequentItemsets(List<Set<Integer>> transactions, int minSupport) {
        
        Map<Set<Integer>, Integer> itemsetCount = new HashMap<>();
        List<Set<Integer>> result = new ArrayList<>();

        // Inicializar con itemsets de tamaño 1
        Set<Integer> allItems = transactions.stream()
            .flatMap(Set::stream)
            .collect(Collectors.toSet());

        List<Set<Integer>> currentItemsets = allItems.stream()
            .map(Collections::singleton)
            .collect(Collectors.toList());

        while (!currentItemsets.isEmpty()) {
            Map<Set<Integer>, Integer> localCount = new HashMap<>();

            for (Set<Integer> itemset : currentItemsets) {
                for (Set<Integer> transaction : transactions) {
                    if (transaction.containsAll(itemset)) {
                        localCount.merge(itemset, 1, Integer::sum);
                    }
                }
            }

            List<Set<Integer>> frequentItemsets = localCount.entrySet().stream()
                .filter(e -> e.getValue() >= minSupport)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

            result.addAll(frequentItemsets);

            // Generar nuevos itemsets combinando los actuales
            currentItemsets = generateNewCombinations(frequentItemsets);
        }

        return result;
    }

    private static List<Set<Integer>> generateNewCombinations(List<Set<Integer>> itemsets) {
        List<Set<Integer>> result = new ArrayList<>();
        int size = itemsets.size();

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                Set<Integer> union = new HashSet<>(itemsets.get(i));
                union.addAll(itemsets.get(j));
                if (union.size() == itemsets.get(i).size() + 1) {
                    result.add(union);
                }
            }
        }
        return result;
    }
    
    public static Set<Integer> recomendar(Set<Integer> librosUsuario, List<Set<Integer>> frecuentes) {
        
        Set<Integer> recomendados = new HashSet<>();
        
        for (Set<Integer> itemset : frecuentes) {
            
            // Si ya los tiene
            if (librosUsuario.containsAll(itemset)) 
                continue;

            Set<Integer> interseccion = new HashSet<>(itemset);
            interseccion.retainAll(librosUsuario);

            if (!interseccion.isEmpty() && interseccion.size() >= itemset.size() - 1) {
                // recomendar los libros que no tenga
                Set<Integer> diferencia = new HashSet<>(itemset);
                diferencia.removeAll(librosUsuario);
                recomendados.addAll(diferencia);
            }
        }
        
        return recomendados;
    }
}
