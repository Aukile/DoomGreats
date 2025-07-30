package com.ankrya.doomgreats.help.json;

import com.google.gson.JsonObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TrophyCreatorGUI extends JFrame {
    private final JComboBox<JsonCreator.TrophyType> typeCombo = new JComboBox<>(JsonCreator.TrophyType.values());
    private final JTextField fileNameField = new JTextField(20); // 新增文件名输入框
    private final JTabbedPane poolTabs = new JTabbedPane();
    private final JButton addPoolButton = new JButton("添加战利品池");
    private final JButton generateButton = new JButton("生成JSON文件");

    public TrophyCreatorGUI() {
        super("战利品表创建工具");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 顶部控制面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("类型:"));
        topPanel.add(typeCombo);
        topPanel.add(new JLabel("文件名:")); // 文件名标签
        topPanel.add(fileNameField);        // 文件名输入框
        topPanel.add(addPoolButton);

        // 生成按钮面板（单独一行）
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(generateButton);

        // 添加战利品池按钮事件
        addPoolButton.addActionListener(e -> addNewPoolTab());

        // 生成按钮事件
        generateButton.addActionListener(e -> generateJson());

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(poolTabs, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH); // 将按钮放在底部
        add(mainPanel);

        // 初始添加一个池
        addNewPoolTab();
    }

    private void addNewPoolTab() {
        int poolIndex = poolTabs.getTabCount() + 1;
        PoolPanel poolPanel = new PoolPanel();
        poolTabs.addTab("战利品池 " + poolIndex, poolPanel);
        poolTabs.setSelectedComponent(poolPanel);
    }

    private void generateJson() {
        try {
            // 获取文件名
            String fileName = fileNameField.getText().trim();
            if (fileName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入文件名！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 收集所有池数据
            List<JsonCreator.TrophyPools> allPools = new ArrayList<>();
            for (int i = 0; i < poolTabs.getTabCount(); i++) {
                PoolPanel panel = (PoolPanel) poolTabs.getComponentAt(i);
                allPools.add(panel.getTrophyPool());
            }

            // 创建JSON对象
            JsonCreator.TrophyType type = (JsonCreator.TrophyType) typeCombo.getSelectedItem();
            JsonObject json = JsonCreator.createTrophy(fileName, type, allPools.toArray(new JsonCreator.TrophyPools[0]));

            // 创建文件
            JsonCreator.createStart(
                    fileName, // 使用用户输入的文件名
                    JsonCreator.GatherType.TROPHIES,
                    json
            );

            JOptionPane.showMessageDialog(this, fileName + "文件生成成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "生成失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // 战利品池面板内部类
    static class PoolPanel extends JPanel {
        private final JSpinner rollsMin = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        private final JSpinner rollsMax = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        private final JSpinner bonusMin = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        private final JSpinner bonusMax = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        private final DefaultTableModel entryModel = new DefaultTableModel();
        private final JTable entryTable = new JTable(entryModel);

        public PoolPanel() {
            setLayout(new BorderLayout(10, 10));
            initPoolControls();
            initEntryTable();
        }

        private void initPoolControls() {
            // 池参数面板
            JPanel poolParamPanel = new JPanel(new GridLayout(2, 4, 5, 5));
            poolParamPanel.setBorder(BorderFactory.createTitledBorder("池参数"));

            poolParamPanel.add(new JLabel("最小抽取次数:"));
            poolParamPanel.add(rollsMin);
            poolParamPanel.add(new JLabel("最大抽取次数:"));
            poolParamPanel.add(rollsMax);
            poolParamPanel.add(new JLabel("最小额外抽取:"));
            poolParamPanel.add(bonusMin);
            poolParamPanel.add(new JLabel("最大额外抽取:"));
            poolParamPanel.add(bonusMax);

            // 条目操作按钮
            JButton addEntryBtn = new JButton("添加条目");
            JButton removeEntryBtn = new JButton("删除条目");

            addEntryBtn.addActionListener(e -> showEntryDialog(null));
            removeEntryBtn.addActionListener(e -> {
                int row = entryTable.getSelectedRow();
                if (row >= 0) entryModel.removeRow(row);
            });

            JPanel btnPanel = new JPanel();
            btnPanel.add(addEntryBtn);
            btnPanel.add(removeEntryBtn);

            JPanel northPanel = new JPanel(new BorderLayout());
            northPanel.add(poolParamPanel, BorderLayout.NORTH);
            northPanel.add(btnPanel, BorderLayout.SOUTH);

            add(northPanel, BorderLayout.NORTH);
        }

        private void initEntryTable() {
            // 配置条目表格
            entryModel.addColumn("物品ID");
            entryModel.addColumn("权重");
            entryModel.addColumn("最小数量");
            entryModel.addColumn("最大数量");
            entryModel.addColumn("附魔最小");
            entryModel.addColumn("附魔最大");
            entryModel.addColumn("时运");
            entryModel.addColumn("爆炸衰减");
            entryModel.addColumn("匹配工具");

            entryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            entryTable.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        int row = entryTable.rowAtPoint(evt.getPoint());
                        showEntryDialog(row);
                    }
                }
            });

            add(new JScrollPane(entryTable), BorderLayout.CENTER);
        }

        private void showEntryDialog(Integer row) {
            EntryDialog dialog = new EntryDialog((JFrame) SwingUtilities.getWindowAncestor(this), row);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                Object[] data = dialog.getEntryData();
                if (row != null) {
                    for (int i = 0; i < data.length; i++) {
                        entryModel.setValueAt(data[i], row, i);
                    }
                } else {
                    entryModel.addRow(data);
                }
            }
        }

        public JsonCreator.TrophyPools getTrophyPool() {
            // 收集条目数据
            List<JsonCreator.TrophyPools.Entry> entries = new ArrayList<>();
            for (int i = 0; i < entryModel.getRowCount(); i++) {
                entries.add(new JsonCreator.TrophyPools.Entry(
                        (String) entryModel.getValueAt(i, 0),
                        (Integer) entryModel.getValueAt(i, 1),
                        (Integer) entryModel.getValueAt(i, 2),
                        (Integer) entryModel.getValueAt(i, 3),
                        (Integer) entryModel.getValueAt(i, 4),
                        (Integer) entryModel.getValueAt(i, 5),
                        (Boolean) entryModel.getValueAt(i, 6),
                        (Boolean) entryModel.getValueAt(i, 7),
                        (Boolean) entryModel.getValueAt(i, 8)
                ));
            }

            return new JsonCreator.TrophyPools(
                    (Integer) rollsMin.getValue(),
                    (Integer) rollsMax.getValue(),
                    (Integer) bonusMin.getValue(),
                    (Integer) bonusMax.getValue(),
                    entries.toArray(new JsonCreator.TrophyPools.Entry[0])
            );
        }
    }

    // 条目编辑对话框内部类
    static class EntryDialog extends JDialog {
        private final JTextField nameField = new JTextField(20);
        private final JSpinner weightSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        private final JSpinner countMinSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 64, 1));
        private final JSpinner countMaxSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 64, 1));
        private final JSpinner enchantMinSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        private final JSpinner enchantMaxSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        private final JCheckBox fortuneCheck = new JCheckBox();
        private final JCheckBox explosionCheck = new JCheckBox();
        private final JCheckBox matchCheck = new JCheckBox();
        private boolean saved = false;

        public EntryDialog(JFrame parent, Integer row) {
            super(parent, "编辑条目", true);
            setSize(400, 300);
            setLocationRelativeTo(parent);
            initUI();
        }

        private void initUI() {
            JPanel panel = new JPanel(new GridLayout(9, 2, 5, 5));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            panel.add(new JLabel("物品注册名:"));
            panel.add(nameField);
            panel.add(new JLabel("权重:"));
            panel.add(weightSpinner);
            panel.add(new JLabel("最小数量:"));
            panel.add(countMinSpinner);
            panel.add(new JLabel("最大数量:"));
            panel.add(countMaxSpinner);
            panel.add(new JLabel("附魔最小等级:"));
            panel.add(enchantMinSpinner);
            panel.add(new JLabel("附魔最大等级:"));
            panel.add(enchantMaxSpinner);
            panel.add(new JLabel("受时运影响:"));
            panel.add(fortuneCheck);
            panel.add(new JLabel("爆炸衰减:"));
            panel.add(explosionCheck);
            panel.add(new JLabel("匹配工具:"));
            panel.add(matchCheck);

            JButton saveBtn = new JButton("保存");
            saveBtn.addActionListener(e -> {
                saved = true;
                dispose();
            });

            JButton cancelBtn = new JButton("取消");
            cancelBtn.addActionListener(e -> dispose());

            JPanel btnPanel = new JPanel();
            btnPanel.add(saveBtn);
            btnPanel.add(cancelBtn);

            add(panel, BorderLayout.CENTER);
            add(btnPanel, BorderLayout.SOUTH);
        }

        public Object[] getEntryData() {
            return new Object[]{
                    nameField.getText(),
                    weightSpinner.getValue(),
                    countMinSpinner.getValue(),
                    countMaxSpinner.getValue(),
                    enchantMinSpinner.getValue(),
                    enchantMaxSpinner.getValue(),
                    fortuneCheck.isSelected(),
                    explosionCheck.isSelected(),
                    matchCheck.isSelected()
            };
        }

        public boolean isSaved() {
            return saved;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TrophyCreatorGUI().setVisible(true));
    }
}