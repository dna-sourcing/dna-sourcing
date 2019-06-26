package com.dna.sourcing.service.time.bctsp;

public enum PkiStatus {
    GRANTED(0),
    GRANTED_WITH_MODS(1),
    REJECTION(2),
    WAITING(3),
    REVOCATION_WARNING(4),
    REVOCATION_NOTIFICATION(5),
    KEY_UPDATE_WARNING(6);

    private final int m_status;

    private PkiStatus(final int p_status) {
        m_status = p_status;
    }

    public static final boolean isGranted(final int p_status) {
        final PkiStatus status = forStatus(p_status);
        if (status == null) {
            return false;
        }

        if (GRANTED.equals(status) || GRANTED_WITH_MODS.equals(status)) {
            return true;
        }

        return false;
    }

    public static final PkiStatus forStatus(final int p_status) {
        for (final PkiStatus status : PkiStatus.values()) {
            if (status.m_status == p_status) {
                return status;
            }
        }

        return null;
    }
}
