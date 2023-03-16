package br.com.sta.enums;

public enum UsuarioTipo {
    PESSOA (0) {
        @Override
        public boolean ehPessoa() {
            return true;
        }
    },
    AUTOR (1) {
        @Override
        public boolean ehAutor() {
            return true;
        }
    },
    AVALIADOR (2) {
        @Override
        public boolean ehAvaliador() {
            return true;
        }
    },
    ORGANIZADOR (3) {
        @Override
        public boolean ehOrganizador() {
            return true;
        }
    };

    UsuarioTipo(int tipo) {this.tipo = tipo;}

    private int tipo;

    public int getTipo() {return this.tipo;}
    
    public boolean ehPessoa() {return false;}

    public boolean ehAutor() {return false;}

    public boolean ehAvaliador() {return false;}

    public boolean ehOrganizador() {return false;}
}
