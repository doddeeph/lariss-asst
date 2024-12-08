import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Connectivity from './connectivity';
import ConnectivityDetail from './connectivity-detail';
import ConnectivityUpdate from './connectivity-update';
import ConnectivityDeleteDialog from './connectivity-delete-dialog';

const ConnectivityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Connectivity />} />
    <Route path="new" element={<ConnectivityUpdate />} />
    <Route path=":id">
      <Route index element={<ConnectivityDetail />} />
      <Route path="edit" element={<ConnectivityUpdate />} />
      <Route path="delete" element={<ConnectivityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ConnectivityRoutes;
