package at.ac.tuwien.mns.group3.mnsg3e3.model;

import android.os.Parcel;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;

public class CellTower {

    private int cellId;
    private int countryCode;
    private int netId;
    private int areaCode;
    private int signalStrength;
    private SignalType type;
    private boolean registered;

    public CellTower(CellInfo cellInfo) {

        registered = cellInfo.isRegistered();

        if (cellInfo instanceof CellInfoGsm) {
            CellSignalStrengthGsm sig_str = ((CellInfoGsm) cellInfo).getCellSignalStrength();
            CellIdentityGsm cell_ident = ((CellInfoGsm) cellInfo).getCellIdentity();

            countryCode = cell_ident.getMcc();
            netId = cell_ident.getMnc();
            areaCode = cell_ident.getLac();
            cellId = cell_ident.getCid();
            signalStrength = sig_str.getDbm();
            type = SignalType.GSM;

        } else if (cellInfo instanceof CellInfoLte) {
            CellSignalStrengthLte sig_str = ((CellInfoLte) cellInfo).getCellSignalStrength();
            CellIdentityLte cell_ident = ((CellInfoLte) cellInfo).getCellIdentity();

            countryCode = cell_ident.getMcc();
            netId = cell_ident.getMnc();
            areaCode = cell_ident.getTac();
            cellId = cell_ident.getCi();
            signalStrength = sig_str.getDbm();
            type = SignalType.LTE;

        } else if (cellInfo instanceof CellInfoWcdma) {
            CellSignalStrengthWcdma sig_str = ((CellInfoWcdma) cellInfo).getCellSignalStrength();
            CellIdentityWcdma cell_ident = ((CellInfoWcdma) cellInfo).getCellIdentity();

            countryCode = cell_ident.getMcc();
            netId = cell_ident.getMnc();
            areaCode = cell_ident.getLac();
            cellId = cell_ident.getCid();
            signalStrength = sig_str.getDbm();
            type = SignalType.UMTS;

        } else {
            // CDMA is ignored, because there is no coutry code and no area code
            return;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CellTower)) {
            return  false;
        }

        CellTower that = (CellTower)obj;

        return that.getCellId() == this.getCellId();
    }


    public SignalType getSignalType() {
        return type;
    }

    public int getCellId() {
        return cellId;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public int getNetId() {
        return netId;
    }

    public int getAreaCode() {
        return areaCode;
    }

    public int getSignalStrength() {
        return signalStrength;
    }

    public boolean isRegistered() {
        return registered;
    }


    public enum SignalType {

        GSM,
        UMTS,
        LTE
    }
}
