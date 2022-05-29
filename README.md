# CabinetMedical

Proiectul este realizat in JAVA si prezinta conceptele AOP-ului. 
In mediul interactiv userul poate intra ca si admin sau ca doctor. 
- Administratorul are posibilitatea de a vedea toate datele despre despre doctori si pacienti, de a "angaja" si "concedia" doctori (a-i adauga si elimina din baza de date), de a le mari salariul. 
- Apeland la intrarea prin doctor mode, acesta trebuie sa isi insereze numele pentru a verifica daca chiar este doctor si el poate sa vada totul despre pacinti, sa insereze, pacienti, appointmenturi, bilete de trimitere (clasa si tabelul din mysql "ticket"), sa adauge boli (clasa si tabelul "disease") pentru un pacient dat si de a da reteta pentru farmacie (prescription). Fiind doctor, acesta poate cauta bolile pe care le-a avut un pacient, biletele date unui pacient in trecut si, desigur, informatiile de ala fiecare programare in parte. 

Toate informatiile necesare sunt retinute in data de date din MySql astfel incat sa se poata ajunge mereu la ultima varianta rezultata din modificari precedente (insert, update, delete).
Ca implementare:
- sunt create clase: interfata, clasa abstracta, mostenire, agregare (un obiect de tip clasa in tr-o alta cllasa), singleton, clase simple, clase service cu functii intre diverse obiecte; 
- sunt folosite lambda expressions si streamuri;
- scriere in fisier (simplu cu writeString si varianta cu FileOutputStream);
- sunt tratate exceptii si sunt aruncate erori.   
