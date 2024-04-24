package bibliotheque.mvc.view;

import bibliotheque.metier.*;
import bibliotheque.mvc.GestionMVC;
import bibliotheque.mvc.controller.ControllerSpecialExemplaire;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


import static bibliotheque.mvc.GestionMVC.LOCATIONS;
import static bibliotheque.mvc.GestionMVC.lv;
import static bibliotheque.utilitaires.Utilitaire.*;
import static bibliotheque.utilitaires.Utilitaire.affListe;

public class ExemplaireViewConsole extends AbstractView<Exemplaire> {

    Scanner sc = new Scanner(System.in);


    @Override
    public void menu() {
        update(controller.getAll());
        List options = Arrays.asList("ajouter", "retirer", "rechercher","modifier","fin");
        do {
            int ch = choixListe(options);

            switch (ch) {
                case 1:
                    ajouter();
                    break;
                case 2:
                    retirer();
                    break;
                case 3:
                    rechercher();
                    break;
                case 4:
                    modifier();
                    break;
                case 5:
                    return;
            }
        } while (true);
    }

    private void retirer() {
        int nl = choixElt(la)-1;
        Exemplaire a = la.get(nl);
        boolean ok = controller.remove(a);
        if(ok) affMsg("exemplaire effacé");
        else affMsg("exemplaire non effacé");
    }

    private void affMsg(String msg) {
        System.out.println(msg);
    }


    public void rechercher() {
        try {
            System.out.println("matricule ");
            String mat = sc.nextLine();
            Exemplaire rech = new Exemplaire(mat,"",null);
            Exemplaire a = controller.search(rech);
            if(a==null) affMsg("exemplaire inconnu");
            else {
                affMsg(a.toString());
                special(a);
            }
        }catch(Exception e){
            System.out.println("erreur : "+e);
        }

    }


    public void modifier() {
        int choix = choixElt(la);
        Exemplaire a = la.get(choix-1);
        do {
            try {
                String description = modifyIfNotBlank("nom", a.getDescriptionEtat());
                a.setDescriptionEtat(description);
                break;
            } catch (Exception e) {
                System.out.println("erreur :" + e);
            }
        }while(true);
        controller.update(a);
    }


    public void ajouter() {
        Exemplaire a;
        do {
            try {
                System.out.println("matricule ");
                String mat = sc.nextLine();
                System.out.println("description ");
                String descr = sc.nextLine();
                System.out.println("ouvrage : ");
                List<Ouvrage> lo = GestionMVC.ov.getAll();
                lo.sort(new Comparator<Ouvrage>() {
                    @Override
                    public int compare(Ouvrage o1, Ouvrage o2) {
                        return o1.getTitre().compareTo(o2.getTitre());
                    }
                });
                //TODO présenter les ouvrages par ordre de titre ==> lambda
                int ch = choixListe(lo);
                a = new Exemplaire(mat, descr,lo.get(ch-1));
                System.out.println("rayon");
                List<Rayon> lr = GestionMVC.rv.getAll();
                lr.sort(new Comparator<Rayon>() {
                    @Override
                    public int compare(Rayon o1, Rayon o2) {
                        return o1.getCodeRayon().compareTo(o2.getCodeRayon());
                    }
                });
                //TODO présenter les rayons par ordre de code ==> lambda
                ch= choixListe(lr);
                a.setRayon(lr.get(ch-1));
                break;
            } catch (Exception e) {
                System.out.println("une erreur est survenue : "+e.getMessage());
            }
        }while(true);
        controller.add(a);
    }

    public void special(Exemplaire a) {

        List options = Arrays.asList("modifier etat", "lecteur actuel", "envoi mail","en location","louer","rendre","fin");
        do {
            int ch = choixListe(options);

            switch (ch) {

                case 1:
                    modifierEtat(a);
                    break;
                case 2:
                    lecteurActuel(a);
                    break;
                case 3:
                    envoiMail(a);
                    break;
                case 4 :
                    enLocation(a);
                    break;
                case 5 :
                    louer(a);
                    break;
                case 6 :
                    rendre(a);
                    break;
                case 7: return;
            }
        } while (true);

    }

    private void rendre(Exemplaire a) {
        GestionMVC.LOCATIONS.remove(a);
   }

    private void louer(Exemplaire a) {
        List<Lecteur> llec= lv.getAll();
        llec.sort(new Comparator<Lecteur>() {
            @Override
            public int compare(Lecteur o1, Lecteur o2) {
                return o1.getNumlecteur()-o2.getNumlecteur();
            }
        });
        int ch = choixListe(llec);
        LOCATIONS.put(a,llec.get(ch-1));
    }


    public void enLocation(Exemplaire ex) {
        boolean loc = ((ControllerSpecialExemplaire)controller).enLocation(ex);
        if(loc) System.out.println("en location");
        else System.out.println("pas en location");
    }


    public void envoiMail(Exemplaire ex) {
        Mail m = new Mail("demo","message de test","01-01-2024");
        ((ControllerSpecialExemplaire)controller).envoiMailLecteurActuel(ex,m);
    }


    public void lecteurActuel(Exemplaire ex) {
        ((ControllerSpecialExemplaire)controller).LecteurActuel(ex);
    }


    public void modifierEtat(Exemplaire ex) {
        System.out.println("nouvel état :");
        String etat = sc.nextLine();
        ((ControllerSpecialExemplaire)controller).modifierEtat(ex,etat) ;
    }

    @Override
    public void affList(List la) {
        affListe(la);
    }
}
