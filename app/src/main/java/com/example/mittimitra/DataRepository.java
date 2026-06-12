package com.example.mittimitra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataRepository {

    public static class FertilizerRecommendation {
        public String condition;
        public String recommendedFertilizer;

        public FertilizerRecommendation(String condition, String recommendedFertilizer) {
            this.condition = condition;
            this.recommendedFertilizer = recommendedFertilizer;
        }
    }

    public static class PesticideRecommendation {
        public String pest;
        public String pesticideName;
        public String recommendation;

        public PesticideRecommendation(String pest, String pesticideName, String recommendation) {
            this.pest = pest;
            this.pesticideName = pesticideName;
            this.recommendation = recommendation;
        }
    }

    // Local data for Fertilizer Recommendations from fertilizer_data.csv
    public static final Map<String, List<FertilizerRecommendation>> FERTILIZER_DATA = new HashMap<>();

    static {
        // Wheat
        List<FertilizerRecommendation> wheatRecs = new ArrayList<>();
        wheatRecs.add(new FertilizerRecommendation("Low N", "Urea / Ammonium Sulphate"));
        wheatRecs.add(new FertilizerRecommendation("High N", "Avoid extra N, use SSP for P"));
        wheatRecs.add(new FertilizerRecommendation("Low P", "Single Super Phosphate (SSP)"));
        wheatRecs.add(new FertilizerRecommendation("High P", "Balanced NPK mix (avoid excess P)"));
        wheatRecs.add(new FertilizerRecommendation("Low K", "Muriate of Potash (MOP)"));
        wheatRecs.add(new FertilizerRecommendation("High K", "Balanced NPK mix"));
        FERTILIZER_DATA.put("Wheat", wheatRecs);

        // Rice
        List<FertilizerRecommendation> riceRecs = new ArrayList<>();
        riceRecs.add(new FertilizerRecommendation("Low N", "Urea (basal + top dressing)"));
        riceRecs.add(new FertilizerRecommendation("High N", "Avoid extra N, prefer DAP"));
        riceRecs.add(new FertilizerRecommendation("Low P", "SSP or DAP"));
        riceRecs.add(new FertilizerRecommendation("High P", "NPK (20-20-0) mix"));
        riceRecs.add(new FertilizerRecommendation("Low K", "Potash (MOP)"));
        riceRecs.add(new FertilizerRecommendation("High K", "N & P fertilizers only"));
        FERTILIZER_DATA.put("Rice", riceRecs);

        // Maize
        List<FertilizerRecommendation> maizeRecs = new ArrayList<>();
        maizeRecs.add(new FertilizerRecommendation("Low N", "Urea, Ammonium Nitrate"));
        maizeRecs.add(new FertilizerRecommendation("High N", "Balanced NPK"));
        maizeRecs.add(new FertilizerRecommendation("Low P", "SSP/DAP"));
        maizeRecs.add(new FertilizerRecommendation("High P", "Balanced NPK"));
        maizeRecs.add(new FertilizerRecommendation("Low K", "MOP"));
        maizeRecs.add(new FertilizerRecommendation("High K", "Avoid K fertilizers"));
        FERTILIZER_DATA.put("Maize", maizeRecs);

        // Cotton
        List<FertilizerRecommendation> cottonRecs = new ArrayList<>();
        cottonRecs.add(new FertilizerRecommendation("Low N", "Urea + FYM"));
        cottonRecs.add(new FertilizerRecommendation("High N", "NPK blend with less N"));
        cottonRecs.add(new FertilizerRecommendation("Low P", "SSP/DAP"));
        cottonRecs.add(new FertilizerRecommendation("High P", "Balanced NPK"));
        cottonRecs.add(new FertilizerRecommendation("Low K", "MOP"));
        cottonRecs.add(new FertilizerRecommendation("High K", "Balanced N & P"));
        FERTILIZER_DATA.put("Cotton", cottonRecs);

        // Potato
        List<FertilizerRecommendation> potatoRecs = new ArrayList<>();
        potatoRecs.add(new FertilizerRecommendation("Low N", "Urea"));
        potatoRecs.add(new FertilizerRecommendation("High N", "Avoid excess N"));
        potatoRecs.add(new FertilizerRecommendation("Low P", "SSP"));
        potatoRecs.add(new FertilizerRecommendation("High P", "Balanced NPK"));
        potatoRecs.add(new FertilizerRecommendation("Low K", "MOP"));
        potatoRecs.add(new FertilizerRecommendation("High K", "N + P fertilizers"));
        FERTILIZER_DATA.put("Potato", potatoRecs);

        // Sugarcane
        List<FertilizerRecommendation> sugarcaneRecs = new ArrayList<>();
        sugarcaneRecs.add(new FertilizerRecommendation("Low N", "Urea (split dose)"));
        sugarcaneRecs.add(new FertilizerRecommendation("High N", "Balanced NPK"));
        sugarcaneRecs.add(new FertilizerRecommendation("Low P", "DAP/SSP"));
        sugarcaneRecs.add(new FertilizerRecommendation("High P", "N + K fertilizers"));
        sugarcaneRecs.add(new FertilizerRecommendation("Low K", "MOP"));
        sugarcaneRecs.add(new FertilizerRecommendation("High K", "N + P fertilizers"));
        FERTILIZER_DATA.put("Sugarcane", sugarcaneRecs);

        // Soyabean
        List<FertilizerRecommendation> soyabeeanRecs = new ArrayList<>();
        sugarcaneRecs.add(new FertilizerRecommendation("Low N", "Urea (split dose)"));
        sugarcaneRecs.add(new FertilizerRecommendation("High N", "Balanced NPK"));
        sugarcaneRecs.add(new FertilizerRecommendation("Low P", "DAP/SSP"));
        sugarcaneRecs.add(new FertilizerRecommendation("High P", "N + K fertilizers"));
        sugarcaneRecs.add(new FertilizerRecommendation("Low K", "MOP"));
        sugarcaneRecs.add(new FertilizerRecommendation("High K", "N + P fertilizers"));
        FERTILIZER_DATA.put("Soyabean", soyabeeanRecs);

        // Tomato
        List<FertilizerRecommendation> tomatoRecs = new ArrayList<>();
        sugarcaneRecs.add(new FertilizerRecommendation("Low N", "Urea (split dose)"));
        sugarcaneRecs.add(new FertilizerRecommendation("High N", "Balanced NPK"));
        sugarcaneRecs.add(new FertilizerRecommendation("Low P", "DAP/SSP"));
        sugarcaneRecs.add(new FertilizerRecommendation("High P", "N + K fertilizers"));
        sugarcaneRecs.add(new FertilizerRecommendation("Low K", "MOP"));
        sugarcaneRecs.add(new FertilizerRecommendation("High K", "N + P fertilizers"));
        FERTILIZER_DATA.put("Tomato", tomatoRecs);

        // Groundnut
        List<FertilizerRecommendation> groundnutRecs = new ArrayList<>();
        sugarcaneRecs.add(new FertilizerRecommendation("Low N", "Urea (split dose)"));
        sugarcaneRecs.add(new FertilizerRecommendation("High N", "Balanced NPK"));
        sugarcaneRecs.add(new FertilizerRecommendation("Low P", "DAP/SSP"));
        sugarcaneRecs.add(new FertilizerRecommendation("High P", "N + K fertilizers"));
        sugarcaneRecs.add(new FertilizerRecommendation("Low K", "MOP"));
        sugarcaneRecs.add(new FertilizerRecommendation("High K", "N + P fertilizers"));
        FERTILIZER_DATA.put("Groundnut", groundnutRecs);

        // Barley
        List<FertilizerRecommendation> barleyRecs = new ArrayList<>();
        sugarcaneRecs.add(new FertilizerRecommendation("Low N", "Urea (split dose)"));
        sugarcaneRecs.add(new FertilizerRecommendation("High N", "Balanced NPK"));
        sugarcaneRecs.add(new FertilizerRecommendation("Low P", "DAP/SSP"));
        sugarcaneRecs.add(new FertilizerRecommendation("High P", "N + K fertilizers"));
        sugarcaneRecs.add(new FertilizerRecommendation("Low K", "MOP"));
        sugarcaneRecs.add(new FertilizerRecommendation("High K", "N + P fertilizers"));
        FERTILIZER_DATA.put("Barley", barleyRecs);

    }

    // Local data for Pesticide Recommendations
    public static final Map<String, List<PesticideRecommendation>> PESTICIDE_DATA = new HashMap<>();

    static {
        // Wheat
        List<PesticideRecommendation> wheatPestRecs = new ArrayList<>();
        wheatPestRecs.add(new PesticideRecommendation("Aphids", "Imidacloprid", "Use Imidacloprid to control aphid infestations."));
        wheatPestRecs.add(new PesticideRecommendation("Rust", "Propiconazole", "Apply Propiconazole fungicide at the first sign of rust."));
        PESTICIDE_DATA.put("Wheat", wheatPestRecs);

        // Rice
        List<PesticideRecommendation> ricePestRecs = new ArrayList<>();
        ricePestRecs.add(new PesticideRecommendation("Stem Borer", "Chlorantraniliprole", "Use Chlorantraniliprole to combat stem borers."));
        ricePestRecs.add(new PesticideRecommendation("Blight", "Fungicide blend", "A broad-spectrum fungicide blend is recommended for blight."));
        PESTICIDE_DATA.put("Rice", ricePestRecs);

        // Maize
        List<PesticideRecommendation> maizePestRecs = new ArrayList<>();
        maizePestRecs.add(new PesticideRecommendation("Corn Borer", "Spinosad", "Spinosad is an effective, low-toxicity option for corn borer."));
        maizePestRecs.add(new PesticideRecommendation("Armyworm", "Pyrethrin", "Apply a Pyrethrin-based pesticide for armyworm control."));
        PESTICIDE_DATA.put("Maize", maizePestRecs);

        // Cotton
        List<PesticideRecommendation> cottonPestRecs = new ArrayList<>();
        cottonPestRecs.add(new PesticideRecommendation("Bollworm", "Chlorpyrifos", "Apply a broad-spectrum insecticide like Chlorpyrifos."));
        cottonPestRecs.add(new PesticideRecommendation("Whitefly", "Buprofezin", "Use Buprofezin to control whitefly populations."));
        PESTICIDE_DATA.put("Cotton", cottonPestRecs);

        // Potato
        List<PesticideRecommendation> potatoPestRecs = new ArrayList<>();
        potatoPestRecs.add(new PesticideRecommendation("Late Blight", "Mancozeb", "Mancozeb is a preventative fungicide for late blight."));
        potatoPestRecs.add(new PesticideRecommendation("Potato Beetle", "Spinosad", "Spinosad is a safe and effective treatment for the potato beetle."));
        PESTICIDE_DATA.put("Potato", potatoPestRecs);

        // Sugarcane
        List<PesticideRecommendation> sugarcanePestRecs = new ArrayList<>();
        sugarcanePestRecs.add(new PesticideRecommendation("Sugarcane Borer", "Acephate", "Apply Acephate to control borer infestations."));
        sugarcanePestRecs.add(new PesticideRecommendation("Rust", "Propiconazole", "Propiconazole is recommended for rust prevention and control."));
        PESTICIDE_DATA.put("Sugarcane", sugarcanePestRecs);
    }

    public static List<String> getPestsForCrop(String crop) {
        List<String> pests = new ArrayList<>();
        if (PESTICIDE_DATA.containsKey(crop)) {
            for (PesticideRecommendation rec : PESTICIDE_DATA.get(crop)) {
                pests.add(rec.pest);
            }
        }
        return pests;
    }
}
