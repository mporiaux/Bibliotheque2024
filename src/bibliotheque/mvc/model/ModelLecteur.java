package bibliotheque.mvc.model;

import bibliotheque.metier.Lecteur;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static bibliotheque.utilitaires.Utilitaire.getDate;

public class ModelLecteur extends DAO<Lecteur> implements DAOSpecialLecteur {
    private List<Lecteur> ldatas = new ArrayList<>();


    @Override
    public Lecteur add( Lecteur elt) {
        boolean present =ldatas.contains(elt);
        if (!present) {
            ldatas.add(elt);
            notifyObservers();
            return elt;
        } else return null;
    }

    @Override
    public boolean remove( Lecteur elt) {
        boolean ok = ldatas.remove(elt);
        notifyObservers();
        return ok;
    }

    @Override
    public Lecteur update(Lecteur elt) {
        int p = ldatas.indexOf(elt);
        if (p < 0) return null;
        ldatas.set(p, elt);
        notifyObservers();
        return elt;
    }

    @Override
    public Lecteur read(Lecteur rech) {
        int p = ldatas.indexOf(rech);
        if(p<0) return null;
        return ldatas.get(p);
    }

    @Override
    public List<Lecteur> getAll() {
        return ldatas;
    }


    @Override
    public void chargementParFichier(){
        File f = new File("d:/nouveauxLecteurs.txt");
        try(FileReader fr = new FileReader(f)){
            Scanner sc = new Scanner(fr);
            while(sc.hasNext()){
                String ligne =sc.nextLine();
                String[] infos = ligne.split(":");
                String nom=infos[0];
                String prenom=infos[1];
                String dns=infos[2];
                LocalDate dn = getDate(dns);
                String adresse=infos[3];
                String mail=infos[4];
                String tel=infos[5];
                add(new Lecteur(nom,prenom,dn,adresse,mail,tel));
            }
        } catch (FileNotFoundException e) {
            System.err.println("erreur de fichier : "+e);
        } catch (IOException e) {
            System.err.println("erreur de fichier : "+e);
        }
        catch(Exception e){
            System.err.println("erreur de fichier : "+e);
        }
    }

}
