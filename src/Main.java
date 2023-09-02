public class Main {
    public static void main(String[] args) {
        new LogicRing().start();
    }
}

/*
 a cada 30 segundos um novo processo deve ser criado
 a cada 25 segundos um processo fazer uma requisição para o coordenador
 a cada 100 segundos o coordenador fica inativo
 a cada 80 segundos um processo da lista de processos fica inativo
 dois processos não podem ter o mesmo ID
 dois processos de eleição não podem acontecer simultaneamente
* */