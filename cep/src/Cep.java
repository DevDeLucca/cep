package cep.src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Cep {
    public static void main(String[] args) {
        String arquivoCSV = ;
        Map<String, Integer> cidades = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length == 3) {
                    String nome = dados[0].trim();
                    String sobrenome = dados[1].trim();
                    String cep = dados[2].trim();
                    Pessoa pessoa = new Pessoa(nome, sobrenome, cep);
                    String cidade = obterNomeCidadePorCEP(cep);
                    if (cidade != null) {
                        cidades.put(cidade, cidades.getOrDefault(cidade, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, Integer> entry : cidades.entrySet()) {
            String cidade = entry.getKey();
            int quantidadePessoas = entry.getValue();
            System.out.println(cidade + ": " + quantidadePessoas + " pessoa(s)");
        }
    }

    public static String obterNomeCidadePorCEP(String cep) {
        try {
            URL url = new URL("https://viacep.com.br/ws/" + cep + "/json/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String json = response.toString();
                if (json.contains("\"erro\": true")) {
                    return null; // CEP inválido
                }

                int startIndex = json.indexOf("\"localidade\": \"") + 15;
                int endIndex = json.indexOf("\",", startIndex);
                return json.substring(startIndex, endIndex) + " - " + json.substring(json.indexOf("\"uf\": \"") + 7, json.indexOf("\",\"ibge\":"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
