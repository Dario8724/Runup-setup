package pt.iade.RunUp.service;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RouteScorer {

    /**
     * Agora recebe coordenadas decodificadas da rota
     */
    public double scoreRoute(
            List<double[]> coords,
            double routeKm,
            double desiredKm,
            boolean preferTrees,
            boolean nearBeach,
            boolean nearPark,
            boolean sunnyRoute
    ) {
        double score = 0;

        // --- 1) Distância ---
        double diff = Math.abs(routeKm - desiredKm);
        score -= diff * 2.0;  // penaliza diferença

        // --- 2) Árvores (mock), mas pode ligar ao DB ou API ---
        if (preferTrees) {
            // aqui você pode melhorar depois com dados reais
            score += estimateTrees(coords) * 40.0;
        }

        // --- 3) Praia ---
        if (nearBeach) {
            score += estimateBeachProximity(coords) * 80.0;
        }

        // --- 4) Parque ---
        if (nearPark) {
            score += estimateParkProximity(coords) * 80.0;
        }

        // --- 5) Sol ---
        if (sunnyRoute) {
            score += estimateSunExposure(coords) * 50.0;
        }

        return score;
    }

    private double estimateTrees(List<double[]> coords) {
        // mock simples por enquanto
        return Math.min(1.0, coords.size() / 3000.0);
    }

    private double estimateBeachProximity(List<double[]> coords) {
        // mock simples — depois pode colocar geofencing real
        return Math.random() * 0.4; 
    }

    private double estimateParkProximity(List<double[]> coords) {
        return Math.random() * 0.4;
    }

    private double estimateSunExposure(List<double[]> coords) {
        return 0.2 + Math.random() * 0.6;
    }
}
