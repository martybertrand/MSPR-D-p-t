import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class generate {

    public static void main(String[] args) {

        List<String> agents = getAgents();

        // on lit pour chaque agent de la liste le fichier .txt et on crée un fichier html avec ses infos a partir du template
        try{
            for (String agent : agents) {
                String htmlString = getTemplate();

                // on récupère les infos sur l'agent
                File agentFile = new File("./txt_source/agents/" + agent + ".txt");
                Scanner scanAgent = new Scanner(agentFile);

                String nomAgent = scanAgent.nextLine();
                String prenomAgent = scanAgent.nextLine();
                String poste = scanAgent.nextLine();
                String htPassword = scanAgent.nextLine();

                scanAgent.nextLine();

                List<String> materiels = new ArrayList<>();
                while (scanAgent.hasNextLine()) {
                    materiels.add(scanAgent.nextLine());
                }
                List<String> transformedMateriel = transformMateriel(materiels);

                // on remplace les tag du template
                htmlString = htmlString.replace("$nom", nomAgent);
                htmlString = htmlString.replace("$prenom", prenomAgent);

                String img_src = "./" + agent + ".jpg";
                htmlString = htmlString.replace("$img_src", img_src);

                String materielString = new String();
                for(String materiel : transformedMateriel){
                    materielString = materielString.concat("<br>" + materiel + "<br>\n");
                }
                htmlString = htmlString.replace("$materiel", materielString);


                // on génère le fichier html avec notre string htmlString qui contient le template complété
                File newHtmlFile = new File("./html_generated/" + agent + ".html");
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(newHtmlFile));
                    bw.write(htmlString);
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode de récupération de la liste des agents qui vérifie si on a le .txt qui correspond à l'agent
    public static List<String> getAgents(){
        List<String> agents = new ArrayList<>();
        try {
            File listeAgents = new File("./txt_source/staff.txt");
            Scanner scanListe = new Scanner(listeAgents);
            int i = 0;
            while (scanListe.hasNextLine()) {
                String a = scanListe.nextLine();
                File tempFile = new File("./txt_source/agents/" + a + ".txt");
                if (tempFile.exists()) {
                    agents.add(a);
                } else {
                    System.err.println("Aucun fichier ne correspond à un agent \"" + a + "\"");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return agents;
    }

    // Méthode de récupération du template
    public static String getTemplate() {
        String htmlString = "";
        try {
            File htmlTemplateFile = new File("./template.html");
            Scanner scanTemplate = new Scanner(htmlTemplateFile);
            while (scanTemplate.hasNextLine()) {
                htmlString = htmlString.concat(scanTemplate.nextLine() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlString;
    }

    // Méthode pour transformer la liste de materiel materielToTransform avec leurs nom complets correspondants
    public static List<String> transformMateriel(List<String> materielToTransform){
        List<String> transformedMaterial = new ArrayList<>();
        try {
            for (String materiel : materielToTransform) {
                File materielFile = new File("./txt_source/liste.txt");
                Scanner scanMateriel = new Scanner(materielFile);
                while(scanMateriel.hasNextLine()){
                    String m[] = scanMateriel.nextLine().split("\t");
                    if(materiel.equals(m[0])) {
                        String nomMateriel = m[1];
                        transformedMaterial.add(nomMateriel);
                    }
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return transformedMaterial;
    }
}
