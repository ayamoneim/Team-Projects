


public class EdgePropertyDialog extends javax.swing.JDialog {
    GraphElements.MyEdge edge;
    
    public EdgePropertyDialog(java.awt.Frame parent, GraphElements.MyEdge edge) {
        super(parent, true);
        initComponents();
        this.edge = edge;
        setTitle("Edge: " + edge.toString());
        this.wtFormattedTextField.setValue(edge.getWeight());
    }
    

    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        capFormattedTextField = new javax.swing.JFormattedTextField();
        wtFormattedTextField = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Edge Properties");
        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonHandler(evt);
            }
        });

      

        jLabel2.setText("Weight:");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jButton1)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                           
                            .add(jLabel2))
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                          
                            .add( layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(wtFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 69, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(wtFormattedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton1))
        );
        pack();
    }

    private void okButtonHandler(java.awt.event.ActionEvent evt) {
        edge.setWeight((Double)this.wtFormattedTextField.getValue());
        dispose();
    }
    
    
    private javax.swing.JFormattedTextField capFormattedTextField;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JFormattedTextField wtFormattedTextField;
    
}
