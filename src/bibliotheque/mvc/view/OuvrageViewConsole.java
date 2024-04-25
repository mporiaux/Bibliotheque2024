package bibliotheque.mvc.view;

import bibliotheque.metier.*;
import bibliotheque.mvc.controller.ControllerSpecialOuvrage;
import bibliotheque.utilitaires.*;

import java.util.*;

import static bibliotheque.mvc.GestionMVC.av;
import static bibliotheque.utilitaires.Utilitaire.*;


public class OuvrageViewConsole extends AbstractView<Ouvrage> {
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
        Ouvrage a = la.get(nl);
        boolean ok = controller.remove(a);
        if(ok) affMsg("ouvrage effacé");
        else affMsg("ouvrage non effacé");
    }

    private void affMsg(String msg) {
        System.out.println(msg);
    }


    public void rechercher() {
        TypeOuvrage[] tto = TypeOuvrage.values();
        List<TypeOuvrage> lto = Arrays.asList(tto);
        int choix = Utilitaire.choixListe(lto);
        Ouvrage o=null;
        switch (choix){
            case 1 :
                System.out.print("isbn :");
                String isbn = sc.nextLine();
                o=new Livre("",0,null,0,"","",isbn,0, TypeLivre.ROMAN,"");
                break;

            case 2 :
                System.out.print("code :");
                int codecd = lireInt();
                o=new CD("",0,null,0,"","",codecd,(byte)0, null);
                break;
            case 3 :
                System.out.print("code :");
                int codedvd = lireInt();
                o=new DVD("",0,null,0,"","",codedvd, null,(byte)0);
                break;

        }
        o=controller.search(o);
        if(o!=null){
            affMsg(o.toString());
            special(o);
        }
        else {
            affMsg("ouvrage inconnu");
        }
    }


    public void modifier() {
        int choix = choixElt(la);
        Ouvrage a = la.get(choix-1);
         do {
            try {
                double ploc =Double.parseDouble(modifyIfNotBlank("prix location",""+a.getPrixLocation()));
                a.setPrixLocation(ploc);
                break;
            } catch (Exception e) {
                System.out.println("erreur :" + e);
            }
        }while(true);
        controller.update(a);
   }


    public void ajouter() {
        TypeOuvrage[] tto = TypeOuvrage.values();
        List<TypeOuvrage> lto = Arrays.asList(tto);
        int choix = Utilitaire.choixListe(lto);
        Ouvrage o = null;
        List<OuvrageFactory> lof = new ArrayList<>(Arrays.asList(new LivreFactory(),new CDFactory(),new DVDFactory()));
        o = lof.get(choix-1).create();
        List<Auteur> la= av.getAll(Comparator.comparing(Auteur::getNom).thenComparing(Auteur::getPrenom));
      //  la.sort(Comparator.comparing(Auteur::getNom).thenComparing(Auteur::getPrenom));
        do {
            Iterator<Auteur> ita = la.iterator();
            while (ita.hasNext()) {
                Auteur a = ita.next();
                if (o.getLauteurs().contains(a)) ita.remove();
            }
            int ch = choixListe(la);
            if (ch == 0) break;
            o.addAuteur(la.get(ch-1));
        }while(true);
        controller.add(o);
    }

    protected void special(Ouvrage o) {


        List options = new ArrayList<>(Arrays.asList("lister exemplaires", "lister exemplaires en location", "lister exemplaires libres","fin"));
        do {
            int ch = choixListe(options);

            switch (ch) {

                case 1:
                    exemplaires(o);
                    break;
                case 2:
                    enLocation(o, true);
                    break;
                case 3:
                    enLocation(o, false);
                    break;

                case 4 :return;
            }
        } while (true);

    }

    public void enLocation(Ouvrage o, boolean enLocation) {
        List<Exemplaire> l= ((ControllerSpecialOuvrage) controller).listerExemplaire(o, enLocation);
        affList(l);
    }

    public void exemplaires(Ouvrage o) {
        List<Exemplaire> l= ((ControllerSpecialOuvrage)controller).listerExemplaire(o);
        affList(l);
    }
    @Override
    public void affList(List la) {
        affListe(la);
    }
}
