package bibliotheque.metier;

import java.io.FileWriter;
import java.io.PrintWriter;

public class Mail {
    private String objet;
    private String message;
    private String dateEnvoi;

    public Mail(String objet, String message, String dateEnvoi) {
        this.objet = objet;
        this.message = message;
        this.dateEnvoi = dateEnvoi;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public void envoi(Lecteur l){
        try(FileWriter fw = new FileWriter("d:/mails/"+l.getMail()+dateEnvoi+".txt",true) ){
            PrintWriter pr = new PrintWriter(fw);
            pr.println("objet :"+objet);
            pr.println(message);
        }
        catch (Exception e){
            System.err.println("une erreur s'est produite : "+e);
        }
    }

    @Override
    public String toString() {
        return "Mail{" +
                "objet='" + objet + '\'' +
                ", message='" + message + '\'' +
                ", dateEnvoi='" + dateEnvoi + '\'' +
                '}';
    }
}
