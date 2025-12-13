package pt.iade.RunUp.service;

import org.springframework.stereotype.Component;
import pt.iade.RunUp.model.dto.RoutePointDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class PredefinedRoutePointsProvider {

    // IDs reais da tua BD
    public static final int ID_BELEM_4K = 37;
    public static final int ID_LISBOA_21K_RIO = 38;
    public static final int ID_LISBOA_21K_CENTRO = 39;

    public List<RoutePointDTO> getPointsForRota(Integer rotaId) {
        if (rotaId == null) return List.of();

        return switch (rotaId) {
            case ID_BELEM_4K -> belem4k();
            case ID_LISBOA_21K_RIO -> lisboa21kRio();
            case ID_LISBOA_21K_CENTRO -> lisboa21kCentro();
            default -> List.of();
        };
    }

    // ---------- AQUI vais pôr as coordenadas reais de cada rota ----------

    private List<RoutePointDTO> belem4k() {
        List<RoutePointDTO> list = new ArrayList<>();

        // TODO: substituir pelas coords reais (exemplo genérico)
        list.add(point(38.695000, -9.206000, 5.0));  // Torre de Belém
        list.add(point(38.696500, -9.203500, 6.0));  // Padrão dos Descobrimentos
        list.add(point(38.697800, -9.200800, 7.0));  // Praça do Império
        list.add(point(38.695000, -9.206000, 5.0));  // volta ao ponto inicial

        return list;
    }

    private List<RoutePointDTO> lisboa21kRio() {
        List<RoutePointDTO> list = new ArrayList<>();

        // TODO: substitui por uma sequência que aproxime bem a rota
        list.add(point(38.697800, -9.230000, 10.0)); // Algés
        list.add(point(38.697000, -9.210000, 8.0));  // Belém
        list.add(point(38.705000, -9.150000, 5.0));  // Cais do Sodré
        list.add(point(38.708000, -9.136000, 4.0));  // Terreiro do Paço
        list.add(point(38.713000, -9.120000, 6.0));  // Santa Apolónia
        // ... acrescentar mais pontos se quiseres deixar a linha mais suave

        return list;
    }

    private List<RoutePointDTO> lisboa21kCentro() {
        List<RoutePointDTO> list = new ArrayList<>();

        // TODO: substituir por coordenadas reais / aproximadas:
        list.add(point(38.725000, -9.150000, 40.0)); // Marquês de Pombal
        list.add(point(38.732000, -9.140000, 60.0)); // Saldanha
        list.add(point(38.748000, -9.150000, 70.0)); // Campo Grande
        list.add(point(38.752000, -9.135000, 80.0)); // Cidade Universitária
        // ...

        return list;
    }

    private RoutePointDTO point(double lat, double lng, double elevation) {
        RoutePointDTO dto = new RoutePointDTO();
        dto.setLatitude(lat);
        dto.setLongitude(lng);
        dto.setElevation(elevation);
        return dto;
    }
}